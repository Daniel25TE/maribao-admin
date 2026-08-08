import axios from 'axios'

// single axios instance so I don't repeat the base URL in every service file
const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
})

export default api
