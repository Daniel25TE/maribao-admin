import { useState } from 'react'
import Sidebar from '../components/common/Sidebar'
import ReservationsSection from '../components/reservations/ReservationsSection'
import DiscountsSection from '../components/discounts/DiscountsSection'
import StatsSection from '../components/stats/StatsSection'
import MediaSection from '../components/media/MediaSection'

// each section is its own component — I swap them out based on what's active in the sidebar
function Dashboard() {
  const [activeSection, setActiveSection] = useState('reservations')

  function renderSection() {
    switch (activeSection) {
      case 'reservations': return <ReservationsSection />
      case 'discounts': return <DiscountsSection />
      case 'media': return <MediaSection />
      case 'stats': return <StatsSection />
      default: return null
    }
  }

  return (
    <div className="flex min-h-screen bg-gray-100">
      <Sidebar activeSection={activeSection} onSectionChange={setActiveSection} />
      <main className="flex-1 p-6">
        {renderSection()}
      </main>
    </div>
  )
}

export default Dashboard
