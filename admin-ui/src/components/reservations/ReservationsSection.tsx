import useReservations from '../../hooks/useReservations'
import { formatDate, formatPrice, getStatusColor } from '../../utils'
import { RESERVATION_STATUSES } from '../../constants'

function ReservationsSection() {
  const { reservations, loading, error, changeStatus, remove } = useReservations()

  if (loading) return <p className="text-gray-500">Loading reservations...</p>
  if (error) return <p className="text-red-500">{error}</p>

  return (
    <div>
      <h2 className="text-2xl font-bold mb-6">Reservations</h2>

      <div className="bg-white rounded-xl shadow overflow-x-auto">
        <table className="min-w-full text-sm">
          <thead className="bg-gray-800 text-white">
            <tr>
              <th className="py-3 px-4 text-left">Guest</th>
              <th className="py-3 px-4 text-left">Email</th>
              <th className="py-3 px-4 text-left">Phone</th>
              <th className="py-3 px-4 text-left">Room</th>
              <th className="py-3 px-4 text-left">Check-in</th>
              <th className="py-3 px-4 text-left">Check-out</th>
              <th className="py-3 px-4 text-left">Total</th>
              <th className="py-3 px-4 text-left">Status</th>
              <th className="py-3 px-4 text-left">Actions</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100">
            {reservations.length === 0 && (
              <tr>
                <td colSpan={9} className="py-6 text-center text-gray-400">No reservations found</td>
              </tr>
            )}
            {reservations.map(reservation => (
              <tr key={reservation.id} className="hover:bg-gray-50">
                <td className="py-3 px-4 font-medium">{reservation.guestName}</td>
                <td className="py-3 px-4 text-gray-600">{reservation.email}</td>
                <td className="py-3 px-4 text-gray-600">{reservation.phone}</td>
                <td className="py-3 px-4">{reservation.roomName}</td>
                <td className="py-3 px-4">{formatDate(reservation.checkIn)}</td>
                <td className="py-3 px-4">{formatDate(reservation.checkOut)}</td>
                <td className="py-3 px-4 font-medium">{formatPrice(reservation.totalPrice)}</td>
                <td className="py-3 px-4">
                  <span className={`px-2 py-1 rounded-full text-xs font-medium ${getStatusColor(reservation.status)}`}>
                    {reservation.status}
                  </span>
                </td>
                <td className="py-3 px-4">
                  <div className="flex gap-2 flex-wrap">
                    {RESERVATION_STATUSES.filter(s => s !== reservation.status).map(status => (
                      <button
                        key={status}
                        onClick={() => changeStatus(reservation.id, status)}
                        className="bg-blue-500 hover:bg-blue-600 text-white px-2 py-1 rounded text-xs transition"
                      >
                        {status}
                      </button>
                    ))}
                    <button
                      onClick={() => {
                        if (confirm('Delete this reservation permanently?')) {
                          remove(reservation.id)
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

export default ReservationsSection
