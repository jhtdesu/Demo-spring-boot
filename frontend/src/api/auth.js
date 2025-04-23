import axios from './axiosConfig'

// gọi API đăng nhập
export const login = async (credentials) => {
  const res = await axios.post('/auth/login', credentials)
  return res.data
}

// gọi API đăng ký
export const register = async (userInfo) => {
  const res = await axios.post('/auth/register', userInfo)
  return res.data
}
