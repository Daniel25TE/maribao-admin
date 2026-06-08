import { useAuth } from '../../context/AuthContext'
import { useNavigate } from 'react-router-dom'

interface SidebarProps {
  activeSection: string
  onSectionChange: (section: string) => void
}

const sections = [
  { id: 'reservations', label: 'Reservations' },
  { id: 'discounts', label: 'Discounts' },
  { id: 'media', label: 'Media' },
  { id: 'stats', label: 'Stats' },
]

function Sidebar({ activeSection, onSectionChange }: SidebarProps) {
  const { logout } = useAuth()
  const navigate = useNavigate()

  function handleLogout() {
    logout()
    navigate('/login')
  }

  return (
    <aside className="bg-white w-64 min-h-screen p-4 border-r border-gray-200 flex flex-col">
      <h1 className="text-xl font-bold mb-2 text-center">Admin Panel</h1>
      <p className="text-center text-gray-400 text-sm mb-6">Hostería Maribao</p>

      <nav className="flex flex-col gap-2 flex-1">
        {sections.map(section => (
          <button
            key={section.id}
            onClick={() => onSectionChange(section.id)}
            className={`text-left px-4 py-2 rounded transition ${
              activeSection === section.id
                ? 'bg-blue-600 text-white'
                : 'hover:bg-gray-100 text-gray-700'
            }`}
          >
            {section.label}
          </button>
        ))}
      </nav>

      <button
        onClick={handleLogout}
        className="mt-6 bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded-md text-center transition"
      >
        Logout
      </button>
    </aside>
  )
}

export default Sidebar
