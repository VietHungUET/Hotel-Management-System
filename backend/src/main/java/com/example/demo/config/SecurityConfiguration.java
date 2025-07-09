package com.example.demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy; // Import này
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.example.demo.service.CustomDetailsService; // Vẫn cần CustomDetailsService của bạn
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter; // Giữ lại CorsFilter nếu bạn muốn bean này

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // Để inject JwtAuthenticationFilter
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter; // Inject JwtAuthenticationFilter
    private final CustomDetailsService customDetailsService; // Inject CustomDetailsService thay vì tạo Bean

    @Bean
    public UserDetailsService userDetailsService() {
        // Trả về một implement của UserDetailsService để tìm kiếm thông tin người dùng từ cơ sở dữ liệu
        // Đã inject CustomDetailsService trực tiếp, không cần tạo bean mới ở đây
        return customDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Bật CORS với cấu hình tùy chỉnh
                .csrf(csrf -> csrf.disable()) // Vô hiệu hóa CSRF vì chúng ta dùng JWT (stateless)
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/login").permitAll() // Cho phép truy cập endpoint login
                        .requestMatchers("/register").permitAll() // Cho phép truy cập endpoint register
                        .requestMatchers("/register/validation").permitAll() // Cho phép truy cập endpoint validation
                        .requestMatchers("/home").permitAll() // Có thể cho phép home hoặc yêu cầu xác thực tùy ý
                        .requestMatchers("/admin/**").hasAuthority("ADMIN") // Sử dụng hasAuthority với vai trò đầy đủ
                        .requestMatchers("/receptionist/**").hasAnyAuthority("ADMIN", "RECEPTIONIST")
                        .requestMatchers("/manager/**").hasAnyAuthority("ADMIN", "MANAGER")
                        .anyRequest().authenticated() // Tất cả các yêu cầu khác đều yêu cầu xác thực
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Cấu hình không sử dụng session
                )
                .authenticationProvider(authenticationProvider()) // Đặt AuthenticationProvider
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // Thêm JWT Filter trước UsernamePasswordAuthenticationFilter
                .logout((logout) -> logout
                        .logoutUrl("/logout") // Định nghĩa URL để logout
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpStatus.OK.value()); // Trả về 200 OK sau khi logout
                        })
                        .invalidateHttpSession(false)
                        .deleteCookies("JSESSIONID")
                )
        ;
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean CorsFilter và CorsConfigurationSource vẫn giữ nguyên nếu bạn cần CORS
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost");
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin("http://localhost");
        configuration.addAllowedOrigin("http://localhost:5173");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // Tạo AuthenticationProvider riêng để sử dụng trong filter chain
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
