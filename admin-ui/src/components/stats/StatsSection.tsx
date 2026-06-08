import { useState } from 'react'
import useStats from '../../hooks/useStats'

// groups daily visits into weekly buckets for the selected month
function getWeeklyBreakdown(perDay: Record<string, number>, month: string) {
  const weeks: Record<number, number> = { 1: 0, 2: 0, 3: 0, 4: 0, 5: 0 }

  Object.entries(perDay).forEach(([date, count]) => {
    if (!date.startsWith(month)) return
    const day = parseInt(date.split('-')[2])
    const week = day <= 7 ? 1 : day <= 14 ? 2 : day <= 21 ? 3 : day <= 28 ? 4 : 5
    weeks[week] += count
  })

  return weeks
}

function StatsSection() {
  const { stats, loading, error } = useStats()
  const [selectedMonth, setSelectedMonth] = useState('')

  if (loading) return <p className="text-gray-500">Loading stats...</p>
  if (error) return <p className="text-red-500">{error}</p>
  if (!stats) return null

  const weeklyBreakdown = selectedMonth ? getWeeklyBreakdown(stats.perDay, selectedMonth) : null
  const monthTotal = weeklyBreakdown ? Object.values(weeklyBreakdown).reduce((a, b) => a + b, 0) : 0

  return (
    <div>
      <h2 className="text-2xl font-bold mb-6">Visit Statistics</h2>

      <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-8">
        <div className="bg-white rounded-xl shadow p-6">
          <p className="text-gray-500 text-sm uppercase tracking-wide mb-1">Total Visits</p>
          <p className="text-4xl font-bold text-blue-600">{stats.total}</p>
        </div>
        <div className="bg-white rounded-xl shadow p-6">
          <p className="text-gray-500 text-sm uppercase tracking-wide mb-1">Visits Today</p>
          <p className="text-4xl font-bold text-green-600">{stats.today}</p>
        </div>
      </div>

      <div className="bg-white rounded-xl shadow p-6">
        <div className="flex items-center gap-4 mb-6">
          <label className="font-medium text-gray-700">Select a month:</label>
          <input
            type="month"
            value={selectedMonth}
            onChange={e => setSelectedMonth(e.target.value)}
            className="border rounded-lg px-3 py-1 outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        {weeklyBreakdown && (
          <div className="overflow-x-auto">
            <table className="min-w-full text-sm border-collapse">
              <thead>
                <tr className="bg-gray-100">
                  <th className="border px-4 py-2 text-left">Week</th>
                  <th className="border px-4 py-2 text-left">Visits</th>
                </tr>
              </thead>
              <tbody>
                {Object.entries(weeklyBreakdown).map(([week, count]) => (
                  <tr key={week} className="hover:bg-gray-50">
                    <td className="border px-4 py-2">Week {week}</td>
                    <td className="border px-4 py-2">{count}</td>
                  </tr>
                ))}
              </tbody>
              <tfoot>
                <tr className="font-bold bg-gray-50">
                  <td className="border px-4 py-2">Total</td>
                  <td className="border px-4 py-2">{monthTotal}</td>
                </tr>
              </tfoot>
            </table>
          </div>
        )}

        {!selectedMonth && (
          <p className="text-gray-400 text-sm">Select a month to see the weekly breakdown</p>
        )}
      </div>
    </div>
  )
}

export default StatsSection
