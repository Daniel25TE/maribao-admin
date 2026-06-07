import axios from 'axios'

// single axios instance so I don't repeat the base URL in every service file
const api = axios.create({
  baseURL: 'http://localhost:8080',
})

export default api
