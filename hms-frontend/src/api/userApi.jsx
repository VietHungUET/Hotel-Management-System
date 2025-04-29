import axiosClient from "./axiosClient";

const userApi = {
  getAll(params) {
    const url = "room/getAll/1";
    return axiosClient.get(url, { params: params });
  },
  getAvailRoom(params) {
    const url = "room/getAvailRoom/1";
    return axiosClient.get(url, { params: params });
  },
  getType(params) {
    const url = "/roomType/getAll";
    return axiosClient.get(url, { params: params });
  },

  addType(data) {
    const url = "/roomType/add";
    return axiosClient.post(url, data);
  },

  updateType(typeId, data) {
    const url = `/roomType/update/${typeId}`;
    return axiosClient.put(url, data);
  },

  deleteType(typeID) {
    const url = `/roomType/delete/${typeID}`;
    return axiosClient.delete(url);
  },

  addRoom(data) {
    const url = "/room/add";
    return axiosClient.post(url, data);
  },

  deleteRoom(roomId) {
    const url = `/room/delete/${roomId}`;
    return axiosClient.delete(url);
  },

  addBooking(data) {
    const url = "/addBookingDetails";
    return axiosClient.post(url, data);
  },

  getAllGuest(params) {
    const url = "/guest/getAll";
    return axiosClient.get(url, { params: params });
  },

  addGuest(data) {
    const url = "/guest/add";
    return axiosClient.post(url, data);
  },

  getRevenue(params) {
    const url = "getRevenue/Payments/" + params;
    console.log(url);
    return axiosClient.get(url, params);
  },

  getLogin({ username, password }) {
    const url = "login";
    return axiosClient.post(url, {
      user_name: username,
      user_password: password,
    });
  },

  getSignUp({ username, password, email, fullName, value }) {
    const url = "register";
    return axiosClient.post(url, {
      user_name: username,
      user_password: password,
      full_name: fullName,
      email: email,
      phone: value,
    });
  },
  getValidCode({ verificationCode }) {
    const url = "register/validation";
    return axiosClient.post(url, {
      userInput: verificationCode,
    });
  },

  getLogout({ sessionId, userName, role }) {
    const url = "logout";
    return axiosClient.post(url, {
      sessionId: sessionId,
      username: userName,
      role: role,
    });
  },

  getHome({ sessionId, userName, role }) {
    const url = "home";
    return axiosClient.post(url, {
      sessionId: sessionId,
      username: userName,
      role: role,
    });
  },
 
};

export default userApi;
