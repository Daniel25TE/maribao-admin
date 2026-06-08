import { useState, useEffect } from 'react'
import type { Reservation } from '../types'
import { getReservations, updateReservationStatus, deleteReservation } from '../services/reservationService'

// handles all reservation state and API calls so the component stays clean
function useReservations() {
  const [reservations, setReservations] = useState<Reservation[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    fetchReservations()
  }, [])

  async function fetchReservations() {
    try {
      setLoading(true)
      const data = await getReservations()
      setReservations(data)
    } catch {
      setError('Failed to load reservations')
    } finally {
      setLoading(false)
    }
  }

  async function changeStatus(id: string, status: string) {
    try {
      const updated = await updateReservationStatus(id, status)
      setReservations(prev => prev.map(r => r.id === id ? updated : r))
    } catch {
      setError('Failed to update status')
    }
  }

  async function remove(id: string) {
    try {
      await deleteReservation(id)
      setReservations(prev => prev.filter(r => r.id !== id))
    } catch {
      setError('Failed to delete reservation')
    }
  }

  return { reservations, loading, error, changeStatus, remove }
}

export default useReservations
