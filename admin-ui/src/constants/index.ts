export const RESERVATION_STATUSES = ['pending', 'confirmed', 'cancelled']
export const ROOM_NAMES = ['Sol', 'Luna', 'Surf', 'Estrella']
export const AUTH_KEY = 'maribao_admin'

// Admin credentials — read from env vars so they're never committed to git
export const ADMIN_USERNAME = import.meta.env.VITE_ADMIN_USERNAME || 'admin'
export const ADMIN_PASSWORD = import.meta.env.VITE_ADMIN_PASSWORD || ''
