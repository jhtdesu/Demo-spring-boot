import axios from "axios";

export default axios.create({
    baseURL: "https://backend-jh-cff06dd28ef7.herokuapp.com",
    headers: {"Content-type": "application/json"},
    withCredentials: true
});