import api from './api'
import type { Discount } from '../types'

// loads all discounts — active ones are the ones the website shows to guests
export async function getDiscounts(): Promise<Discount[]> {
  const response = await api.get('/api/discounts')
  return response.data
}

// saves a new discount to the database
export async function createDiscount(discount: Discount): Promise<Discount> {
  const response = await api.post('/api/discounts', discount)
  return response.data
}

// flips a discount on or off without deleting it
export async function toggleDiscount(id: string): Promise<Discount> {
  const response = await api.patch(`/api/discounts/${id}/toggle`)
  return response.data
}

// removes a discount for good
export async function deleteDiscount(id: string): Promise<void> {
  await api.delete(`/api/discounts/${id}`)
}
