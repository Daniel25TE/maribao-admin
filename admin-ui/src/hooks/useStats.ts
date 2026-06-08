import { useState, useEffect } from 'react'
import { getStatsSummary } from '../services/statsService'
import type { StatsSummary } from '../services/statsService'

// loads the visit stats once when the stats section mounts
function useStats() {
  const [stats, setStats] = useState<StatsSummary | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    async function fetchStats() {
      try {
        const data = await getStatsSummary()
        setStats(data)
      } catch {
        setError('Failed to load stats')
      } finally {
        setLoading(false)
      }
    }
    fetchStats()
  }, [])

  return { stats, loading, error }
}

export default useStats
