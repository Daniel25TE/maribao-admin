import { useState, useEffect } from 'react'
import type { Discount } from '../types'
import { getDiscounts, createDiscount, toggleDiscount, deleteDiscount } from '../services/discountService'

// handles all discount state and keeps the UI in sync with the API
function useDiscounts() {
  const [discounts, setDiscounts] = useState<Discount[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    fetchDiscounts()
  }, [])

  async function fetchDiscounts() {
    try {
      setLoading(true)
      const data = await getDiscounts()
      setDiscounts(data)
    } catch {
      setError('Failed to load discounts')
    } finally {
      setLoading(false)
    }
  }

  async function add(discount: Discount) {
    try {
      const created = await createDiscount(discount)
      setDiscounts(prev => [...prev, created])
    } catch {
      setError('Failed to create discount')
    }
  }

  async function toggle(id: string) {
    try {
      const updated = await toggleDiscount(id)
      setDiscounts(prev => prev.map(d => d.id === id ? updated : d))
    } catch {
      setError('Failed to toggle discount')
    }
  }

  async function remove(id: string) {
    try {
      await deleteDiscount(id)
      setDiscounts(prev => prev.filter(d => d.id !== id))
    } catch {
      setError('Failed to delete discount')
    }
  }

  return { discounts, loading, error, add, toggle, remove }
}

export default useDiscounts
