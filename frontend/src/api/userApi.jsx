import axiosClient from "./axiosClient";

const userApi = {
  getAll() {
    const url = `/api/v1/rooms`;
    return axiosClient.get(url);
  },
  getAvailRoom(params) {
    const url = `/api/v1/rooms/available`;
    return axiosClient.get(url, { params: params });
  },
  getType() {
    const url = "/api/v1/room-types";
    return axiosClient.get(url);
  },

  addType(data) {
    const url = "/api/v1/room-types";
    return axiosClient.post(url, data);
  },

  updateType(typeId, data) {
    const url = `/api/v1/room-types/${typeId}`;
    return axiosClient.put(url, data);
  },

  deleteType(typeID) {
    const url = `/api/v1/room-types/${typeID}`;
    return axiosClient.delete(url);
  },

  addRoom(data) {
    const url = "/api/v1/rooms";
    return axiosClient.post(url, data);
  },

  deleteRoom(roomId) {
    const url = `/api/v1/rooms/${roomId}`;
    return axiosClient.delete(url);
  },

  addBooking(data) {
    const url = "/api/v1/bookings";
    return axiosClient.post(url, data);
  },

  getAllGuest(params) {
    const url = "/api/v1/guests";
    return axiosClient.get(url, { params: params });
  },

  addGuest(data) {
    const url = "/api/v1/guests";
    return axiosClient.post(url, data);
  },

  getRevenue(params) {
    const url = `/api/v1/payments/revenue/${params}`;
    console.log(url);
    return axiosClient.get(url);
  },

  getLogin({ username, password }) {
    const url = "/api/v1/auth/login";
    return axiosClient.post(url, {
      userName: username,
      password: password,
    });
  },

  getSignUp({ username, password, email, fullName, value }) {
    const url = "/api/v1/users/register";
    return axiosClient.post(url, {
      userName: username,
      password: password,
      fullName: fullName,
      email: email,
      phone: value,
    });
  },
  getValidCode({ verificationCode, user }) {
    const url = "/api/v1/users/register/validate";
    return axiosClient.post(url, {
      validationCode: verificationCode,
      user: user
    });
  },

  getLogout() {
    // For JWT auth, logout is client-side only (remove token from localStorage)
    // No backend call needed
    localStorage.removeItem("token");
    return Promise.resolve({ success: true });
  },

};

export default userApi;
