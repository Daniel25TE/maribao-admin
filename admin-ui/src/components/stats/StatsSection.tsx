import { useState } from 'react'
import useStats from '../../hooks/useStats'

const MONTHS = [
  'Jan', 'Feb', 'Mar', 'Apr',
  'May', 'Jun', 'Jul', 'Aug',
  'Sep', 'Oct', 'Nov', 'Dec',
]

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
  const currentYear = new Date().getFullYear()
  const [year, setYear] = useState(currentYear)
  const [selectedMonth, setSelectedMonth] = useState('')

  if (loading) return <p className="text-gray-500">Loading stats...</p>
  if (error) return <p className="text-red-500">{error}</p>
  if (!stats) return null

  const weeklyBreakdown = selectedMonth ? getWeeklyBreakdown(stats.perDay, selectedMonth) : null
  const monthTotal = weeklyBreakdown ? Object.values(weeklyBreakdown).reduce((a, b) => a + b, 0) : 0

  function selectMonth(monthIndex: number) {
    const mm = String(monthIndex + 1).padStart(2, '0')
    const value = `${year}-${mm}`
    setSelectedMonth(prev => prev === value ? '' : value)
  }

  const selectedYear = selectedMonth ? parseInt(selectedMonth.split('-')[0]) : null
  const selectedMonthIndex = selectedMonth ? parseInt(selectedMonth.split('-')[1]) - 1 : null

  return (
    <div>
      <h2 className="text-2xl font-bold mb-6">Visit Statistics</h2>

      <div className="grid grid-cols-2 gap-4 mb-6">
        <div className="bg-white rounded-xl shadow p-4 desk:p-6">
          <p className="text-gray-500 text-xs desk:text-sm uppercase tracking-wide mb-1">Total Visits</p>
          <p className="text-3xl desk:text-4xl font-bold text-blue-600">{stats.total}</p>
        </div>
        <div className="bg-white rounded-xl shadow p-4 desk:p-6">
          <p className="text-gray-500 text-xs desk:text-sm uppercase tracking-wide mb-1">Visits Today</p>
          <p className="text-3xl desk:text-4xl font-bold text-green-600">{stats.today}</p>
        </div>
      </div>

      {/* Month picker */}
      <div className="bg-white rounded-xl shadow p-4 desk:p-6 mb-6">
        <p className="text-sm font-medium text-gray-500 mb-3">Select a month to see weekly breakdown</p>

        {/* Year navigation */}
        <div className="flex items-center justify-between mb-4">
          <button
            onClick={() => { setYear(y => y - 1); setSelectedMonth('') }}
            className="p-2 rounded-lg hover:bg-gray-100 active:bg-gray-200 transition touch-manipulation"
            aria-label="Previous year"
          >
            <svg className="w-5 h-5 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
            </svg>
          </button>
          <span className="text-xl font-bold text-gray-800">{year}</span>
          <button
            onClick={() => { setYear(y => y + 1); setSelectedMonth('') }}
            disabled={year >= currentYear}
            className="p-2 rounded-lg hover:bg-gray-100 active:bg-gray-200 transition touch-manipulation disabled:opacity-30"
            aria-label="Next year"
          >
            <svg className="w-5 h-5 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
            </svg>
          </button>
        </div>

        {/* Month grid */}
        <div className="grid grid-cols-3 desk:grid-cols-6 gap-2">
          {MONTHS.map((label, i) => {
            const isSelected = selectedYear === year && selectedMonthIndex === i
            const isFuture = year === currentYear && i > new Date().getMonth()
            return (
              <button
                key={label}
                onClick={() => selectMonth(i)}
                disabled={isFuture}
                className={`py-2.5 rounded-xl text-sm font-medium transition touch-manipulation
                  ${isSelected
                    ? 'bg-blue-600 text-white shadow-sm'
                    : isFuture
                      ? 'text-gray-300 cursor-not-allowed'
                      : 'bg-gray-100 hover:bg-blue-50 hover:text-blue-600 active:bg-blue-100 text-gray-700'
                  }`}
              >
                {label}
              </button>
            )
          })}
        </div>
      </div>

      {/* Weekly breakdown */}
      {weeklyBreakdown && (
        <div className="bg-white rounded-xl shadow p-4 desk:p-6">
          <p className="font-semibold text-gray-800 mb-4">
            {MONTHS[selectedMonthIndex!]} {selectedYear} — weekly breakdown
          </p>

          {/* Mobile: cards */}
          <div className="flex flex-col gap-3 desk:hidden">
            {Object.entries(weeklyBreakdown).map(([week, count]) => (
              <div key={week} className="flex items-center justify-between bg-gray-50 rounded-lg px-4 py-3">
                <span className="font-medium text-gray-700">Week {week}</span>
                <span className="text-blue-600 font-bold text-lg">{count}</span>
              </div>
            ))}
            <div className="flex items-center justify-between bg-blue-50 rounded-lg px-4 py-3 border border-blue-100">
              <span className="font-bold text-gray-800">Total</span>
              <span className="text-blue-700 font-bold text-lg">{monthTotal}</span>
            </div>
          </div>

          {/* Desktop: table */}
          <div className="hidden desk:block overflow-x-auto">
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
        </div>
      )}
    </div>
  )
}

export default StatsSection
