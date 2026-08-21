import { useAuth } from '../../context/AuthContext'
import { useNavigate } from 'react-router-dom'

interface SidebarProps {
  activeSection: string
  onSectionChange: (section: string) => void
  onClose?: () => void
}

const sections = [
  {
    id: 'reservations',
    label: 'Reservations',
    icon: (
      <svg className="w-5 h-5 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
      </svg>
    ),
  },
  {
    id: 'discounts',
    label: 'Discounts',
    icon: (
      <svg className="w-5 h-5 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 7h.01M17 17h.01M7 17L17 7M7 7a2 2 0 100 4 2 2 0 000-4zm10 10a2 2 0 100 4 2 2 0 000-4z" />
      </svg>
    ),
  },
  {
    id: 'media',
    label: 'Media',
    icon: (
      <svg className="w-5 h-5 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
      </svg>
    ),
  },
  {
    id: 'stats',
    label: 'Stats',
    icon: (
      <svg className="w-5 h-5 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
      </svg>
    ),
  },
]

function Sidebar({ activeSection, onSectionChange, onClose }: SidebarProps) {
  const { logout } = useAuth()
  const navigate = useNavigate()

  function handleLogout() {
    logout()
    navigate('/login')
  }

  return (
    <aside className="bg-white w-72 desk:w-64 h-full min-h-screen p-4 border-r border-gray-200 flex flex-col shadow-xl desk:shadow-none">
      {/* Header row: title + close button (mobile only) */}
      <div className="flex items-center justify-between mb-1">
        <div>
          <h1 className="text-lg font-bold text-gray-800 leading-tight">Admin Panel</h1>
          <p className="text-xs text-gray-400">Hostería Maribao</p>
        </div>
        {onClose && (
          <button
            onClick={onClose}
            className="desk:hidden p-2 rounded-lg hover:bg-gray-100 active:bg-gray-200 transition touch-manipulation"
            aria-label="Close menu"
          >
            <svg className="w-5 h-5 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        )}
      </div>

      <div className="border-t border-gray-100 my-4" />

      <nav className="flex flex-col gap-1 flex-1">
        {sections.map(section => (
          <button
            key={section.id}
            onClick={() => onSectionChange(section.id)}
            className={`flex items-center gap-3 text-left px-4 py-3 rounded-xl transition touch-manipulation ${
              activeSection === section.id
                ? 'bg-blue-600 text-white shadow-sm'
                : 'hover:bg-gray-100 active:bg-gray-200 text-gray-700'
            }`}
          >
            {section.icon}
            <span className="font-medium">{section.label}</span>
          </button>
        ))}
      </nav>

      <button
        onClick={handleLogout}
        className="mt-4 flex items-center justify-center gap-2 bg-red-50 hover:bg-red-100 active:bg-red-200 text-red-600 px-4 py-3 rounded-xl font-medium transition touch-manipulation"
      >
        <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
        </svg>
        Logout
      </button>
    </aside>
  )
}

export default Sidebar
