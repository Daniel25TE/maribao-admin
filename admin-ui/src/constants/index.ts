// where the Spring Boot API is running locally
export const API_BASE_URL = 'http://localhost:8080'

// these have to match exactly what the Spring Boot API expects
export const RESERVATION_STATUSES = ['pending', 'confirmed', 'cancelled']

// the four rooms at the hotel — same names as on maribao.com
export const ROOM_NAMES = ['Sol', 'Luna', 'Surf', 'Estrella']

// key I use to store login state in localStorage
export const AUTH_KEY = 'maribao_admin'

// hardcoded for now — I'll replace this with real auth later
export const ADMIN_USERNAME = 'admin'
export const ADMIN_PASSWORD = 'maribao2024'
