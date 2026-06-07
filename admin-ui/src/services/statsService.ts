import api from './api'

export interface StatsSummary {
  total: number
  today: number
  perDay: Record<string, number>
  perMonth: Record<string, number>
}

// fetches everything at once — used to populate the whole stats section
export async function getStatsSummary(): Promise<StatsSummary> {
  const response = await api.get('/api/stats/summary')
  return response.data
}
