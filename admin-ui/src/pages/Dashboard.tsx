import { useState } from 'react'
import Sidebar from '../components/common/Sidebar'
import ReservationsSection from '../components/reservations/ReservationsSection'
import DiscountsSection from '../components/discounts/DiscountsSection'
import StatsSection from '../components/stats/StatsSection'
import MediaSection from '../components/media/MediaSection'

const sectionLabels: Record<string, string> = {
  reservations: 'Reservations',
  discounts: 'Discounts',
  media: 'Media',
  stats: 'Stats',
}

function Dashboard() {
  const [activeSection, setActiveSection] = useState('reservations')
  const [drawerOpen, setDrawerOpen] = useState(false)

  function renderSection() {
    switch (activeSection) {
      case 'reservations': return <ReservationsSection />
      case 'discounts': return <DiscountsSection />
      case 'media': return <MediaSection />
      case 'stats': return <StatsSection />
      default: return null
    }
  }

  function handleSectionChange(section: string) {
    setActiveSection(section)
    setDrawerOpen(false)
  }

  return (
    <div className="min-h-screen bg-gray-100 flex">
      {/* Mobile overlay */}
      {drawerOpen && (
        <div
          className="fixed inset-0 z-40 bg-black/50 desk:hidden"
          onClick={() => setDrawerOpen(false)}
        />
      )}

      {/* Sidebar: fixed drawer on mobile, static on desktop */}
      <div
        className={`
          fixed inset-y-0 left-0 z-50 transition-transform duration-300 ease-in-out
          desk:static desk:z-auto desk:translate-x-0
          ${drawerOpen ? 'translate-x-0' : '-translate-x-full'}
        `}
      >
        <Sidebar
          activeSection={activeSection}
          onSectionChange={handleSectionChange}
          onClose={() => setDrawerOpen(false)}
        />
      </div>

      {/* Content area */}
      <div className="flex-1 flex flex-col min-w-0">
        {/* Mobile header */}
        <header className="desk:hidden sticky top-0 z-30 bg-white border-b border-gray-200 h-14 flex items-center px-4">
          <button
            onClick={() => setDrawerOpen(true)}
            className="p-2 rounded-lg hover:bg-gray-100 active:bg-gray-200 transition touch-manipulation"
            aria-label="Open menu"
          >
            <svg className="w-6 h-6 text-gray-700" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
            </svg>
          </button>
          <div className="flex-1 text-center">
            <p className="font-bold text-gray-800 text-sm leading-tight">Hostería Maribao</p>
            <p className="text-xs text-gray-400">{sectionLabels[activeSection]}</p>
          </div>
          {/* Spacer to visually center the title */}
          <div className="w-10" />
        </header>

        <main className="flex-1 p-4 desk:p-6">
          {renderSection()}
        </main>
      </div>
    </div>
  )
}

export default Dashboard
