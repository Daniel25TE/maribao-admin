import { useState } from 'react'
import useDiscounts from '../../hooks/useDiscounts'
import { formatDate } from '../../utils'
import { v4 as uuidv4 } from 'uuid'

function DiscountsSection() {
  const { discounts, loading, error, add, toggle, remove } = useDiscounts()
  const [showForm, setShowForm] = useState(false)
  const [date, setDate] = useState('')
  const [percentage, setPercentage] = useState('')
  const [description, setDescription] = useState('')

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    await add({
      id: uuidv4(),
      date,
      percentage: parseFloat(percentage),
      description,
      active: true,
    })
    setDate('')
    setPercentage('')
    setDescription('')
    setShowForm(false)
  }

  if (loading) return <p className="text-gray-500">Loading discounts...</p>
  if (error) return <p className="text-red-500">{error}</p>

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <h2 className="text-2xl font-bold">Discounts</h2>
        <button
          onClick={() => setShowForm(!showForm)}
          className="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-lg transition"
        >
          {showForm ? 'Cancel' : '+ Add Discount'}
        </button>
      </div>

      {showForm && (
        <form onSubmit={handleSubmit} className="bg-white p-6 rounded-xl shadow mb-6 flex flex-col gap-4">
          <h3 className="font-semibold text-gray-700">New Discount</h3>
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
            <div>
              <label className="block text-sm text-gray-600 mb-1">Date</label>
              <input
                type="date"
                value={date}
                onChange={e => setDate(e.target.value)}
                className="w-full border rounded-lg p-2 outline-none focus:ring-2 focus:ring-blue-500"
                required
              />
            </div>
            <div>
              <label className="block text-sm text-gray-600 mb-1">Percentage (%)</label>
              <input
                type="number"
                value={percentage}
                onChange={e => setPercentage(e.target.value)}
                className="w-full border rounded-lg p-2 outline-none focus:ring-2 focus:ring-blue-500"
                min="1"
                max="100"
                required
              />
            </div>
            <div>
              <label className="block text-sm text-gray-600 mb-1">Description</label>
              <input
                type="text"
                value={description}
                onChange={e => setDescription(e.target.value)}
                className="w-full border rounded-lg p-2 outline-none focus:ring-2 focus:ring-blue-500"
                required
              />
            </div>
          </div>
          <button
            type="submit"
            className="self-start bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg transition"
          >
            Save
          </button>
        </form>
      )}

      <div className="bg-white rounded-xl shadow overflow-x-auto">
        <table className="min-w-full text-sm">
          <thead className="bg-gray-800 text-white">
            <tr>
              <th className="py-3 px-4 text-left">Date</th>
              <th className="py-3 px-4 text-left">Percentage</th>
              <th className="py-3 px-4 text-left">Description</th>
              <th className="py-3 px-4 text-left">Active</th>
              <th className="py-3 px-4 text-left">Actions</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100">
            {discounts.length === 0 && (
              <tr>
                <td colSpan={5} className="py-6 text-center text-gray-400">No discounts found</td>
              </tr>
            )}
            {discounts.map(discount => (
              <tr key={discount.id} className="hover:bg-gray-50">
                <td className="py-3 px-4">{formatDate(discount.date)}</td>
                <td className="py-3 px-4">{discount.percentage}%</td>
                <td className="py-3 px-4 text-gray-600">{discount.description}</td>
                <td className="py-3 px-4">
                  <span className={`px-2 py-1 rounded-full text-xs font-medium ${discount.active ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-500'}`}>
                    {discount.active ? 'Active' : 'Inactive'}
                  </span>
                </td>
                <td className="py-3 px-4">
                  <div className="flex gap-2">
                    <button
                      onClick={() => toggle(discount.id)}
                      className="bg-yellow-400 hover:bg-yellow-500 text-white px-2 py-1 rounded text-xs transition"
                    >
                      {discount.active ? 'Deactivate' : 'Activate'}
                    </button>
                    <button
                      onClick={() => {
                        if (confirm('Delete this discount permanently?')) {
                          remove(discount.id)
                        }
                      }}
                      className="bg-red-500 hover:bg-red-600 text-white px-2 py-1 rounded text-xs transition"
                    >
                      Delete
                    </button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}

export default DiscountsSection
