import api from './api'
import type { Reservation } from '../types'

// loads all reservations — used in the admin panel table
export async function getReservations(): Promise<Reservation[]> {
  const response = await api.get('/api/reservations')
  return response.data
}

// changes a reservation's status — e.g. from pending to confirmed
export async function updateReservationStatus(id: string, status: string): Promise<Reservation> {
  const response = await api.patch(`/api/reservations/${id}/status`, { status })
  return response.data
}

// removes a reservation for good — no undo
export async function deleteReservation(id: string): Promise<void> {
  await api.delete(`/api/reservations/${id}`)
}
