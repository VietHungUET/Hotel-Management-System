import axios from "axios";

const axiosClient = axios.create({

  baseURL: "http://localhost:8080",
  withCredentials: true,
  headers: {
    "Content-Type": "application/json",
  },

});

//Interceptors
// Add a request interceptor

axiosClient.interceptors.request.use(
  function (config) {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  function (error) {
    // Do something with request error
    return Promise.reject(error);
  }
);

// Add a response interceptor
axiosClient.interceptors.response.use(
  function (response) {
    // Backend now wraps responses in ApiResponse: { message, data }
    // Automatically unwrap to just the data field
    if (response.data && response.data.data !== undefined) {
      return response.data.data;
    }
    // Fallback for responses without ApiResponse wrapper
    return response.data;
  },
  function (error) {
    // Any status codes that falls outside the range of 2xx cause this function to trigger
    // Do something with response error
    return Promise.reject(error);
  }
);


export default axiosClient;
