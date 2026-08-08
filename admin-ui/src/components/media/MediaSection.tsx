import { useState, useEffect } from 'react'
import useMedia from '../../hooks/useMedia'
import { ROOM_NAMES } from '../../constants'

function MediaSection() {
  const { photos, videos, loading, uploading, error, addPhoto, removePhoto, addVideo, removeVideo } = useMedia()

  const [lightboxUrl, setLightboxUrl] = useState<string | null>(null)
  const [lightboxAlt, setLightboxAlt] = useState('')

  useEffect(() => {
    function onKey(e: KeyboardEvent) {
      if (e.key === 'Escape') setLightboxUrl(null)
    }
    window.addEventListener('keydown', onKey)
    return () => window.removeEventListener('keydown', onKey)
  }, [])

  // photo upload form state
  const [photoFile, setPhotoFile] = useState<File | null>(null)
  const [photoAltText, setPhotoAltText] = useState('')
  const [photoRoom, setPhotoRoom] = useState(ROOM_NAMES[0])

  // video upload form state
  const [videoFile, setVideoFile] = useState<File | null>(null)
  const [videoAltText, setVideoAltText] = useState('')
  const [videoTitle, setVideoTitle] = useState('')
  const [videoRoom, setVideoRoom] = useState(ROOM_NAMES[0])

  async function handlePhotoSubmit(e: React.FormEvent) {
    e.preventDefault()
    if (!photoFile) return
    await addPhoto(photoFile, photoAltText, photoRoom)
    setPhotoFile(null)
    setPhotoAltText('')
    setPhotoRoom(ROOM_NAMES[0])
    const input = document.getElementById('photo-input') as HTMLInputElement
    if (input) input.value = ''
  }

  async function handleVideoSubmit(e: React.FormEvent) {
    e.preventDefault()
    if (!videoFile) return
    await addVideo(videoFile, videoAltText, videoTitle, videoRoom)
    setVideoFile(null)
    setVideoAltText('')
    setVideoTitle('')
    setVideoRoom(ROOM_NAMES[0])
    const input = document.getElementById('video-input') as HTMLInputElement
    if (input) input.value = ''
  }

  if (loading) return <p className="text-gray-500">Loading media...</p>
  if (error) return <p className="text-red-500">{error}</p>

  return (
    <div className="space-y-10">
      <h2 className="text-2xl font-bold">Media</h2>

      {uploading && (
        <div className="bg-blue-50 text-blue-600 px-4 py-2 rounded-lg text-sm">
          Uploading to Cloudinary...
        </div>
      )}

      {/* PHOTOS */}
      <section>
        <h3 className="text-lg font-semibold mb-4">Photos</h3>

        <form onSubmit={handlePhotoSubmit} className="bg-white p-6 rounded-xl shadow mb-6 flex flex-col gap-4">
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
            <div>
              <label className="block text-sm text-gray-600 mb-1">Photo file</label>
              <input
                id="photo-input"
                type="file"
                accept="image/*"
                onChange={e => setPhotoFile(e.target.files?.[0] || null)}
                className="w-full text-sm text-gray-600"
                required
              />
            </div>
            <div>
              <label className="block text-sm text-gray-600 mb-1">Alt text</label>
              <input
                type="text"
                value={photoAltText}
                onChange={e => setPhotoAltText(e.target.value)}
                className="w-full border rounded-lg p-2 outline-none focus:ring-2 focus:ring-blue-500"
                required
              />
            </div>
            <div>
              <label className="block text-sm text-gray-600 mb-1">Room</label>
              <select
                value={photoRoom}
                onChange={e => setPhotoRoom(e.target.value)}
                className="w-full border rounded-lg p-2 outline-none focus:ring-2 focus:ring-blue-500"
              >
                {ROOM_NAMES.map(r => <option key={r} value={r}>{r}</option>)}
              </select>
            </div>
          </div>
          <button
            type="submit"
            disabled={uploading}
            className="self-start bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg transition disabled:opacity-50"
          >
            Upload Photo
          </button>
        </form>

        <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 gap-4">
          {photos.length === 0 && <p className="text-gray-400 text-sm col-span-4">No photos uploaded yet</p>}
          {photos.map(photo => (
            <div key={photo.id} className="relative group bg-white rounded-xl shadow overflow-hidden">
              <img
                src={photo.url}
                alt={photo.altText}
                className="w-full h-40 object-cover cursor-zoom-in"
                onClick={() => { setLightboxUrl(photo.url); setLightboxAlt(photo.altText) }}
              />
              <div className="p-2">
                <p className="text-xs text-gray-500 truncate">{photo.altText}</p>
                <p className="text-xs text-blue-500">{photo.room}</p>
              </div>
              <button
                onClick={() => { if (confirm('Delete this photo?')) removePhoto(photo.id) }}
                className="absolute top-2 right-2 bg-red-500 text-white rounded-full w-6 h-6 text-xs opacity-0 group-hover:opacity-100 transition"
              >
                ✕
              </button>
            </div>
          ))}
        </div>
      </section>

      {/* VIDEOS */}
      <section>
        <h3 className="text-lg font-semibold mb-4">Videos</h3>

        <form onSubmit={handleVideoSubmit} className="bg-white p-6 rounded-xl shadow mb-6 flex flex-col gap-4">
          <div className="grid grid-cols-1 sm:grid-cols-4 gap-4">
            <div>
              <label className="block text-sm text-gray-600 mb-1">Video file</label>
              <input
                id="video-input"
                type="file"
                accept="video/*"
                onChange={e => setVideoFile(e.target.files?.[0] || null)}
                className="w-full text-sm text-gray-600"
                required
              />
            </div>
            <div>
              <label className="block text-sm text-gray-600 mb-1">Title</label>
              <input
                type="text"
                value={videoTitle}
                onChange={e => setVideoTitle(e.target.value)}
                className="w-full border rounded-lg p-2 outline-none focus:ring-2 focus:ring-blue-500"
                required
              />
            </div>
            <div>
              <label className="block text-sm text-gray-600 mb-1">Alt text</label>
              <input
                type="text"
                value={videoAltText}
                onChange={e => setVideoAltText(e.target.value)}
                className="w-full border rounded-lg p-2 outline-none focus:ring-2 focus:ring-blue-500"
                required
              />
            </div>
            <div>
              <label className="block text-sm text-gray-600 mb-1">Room</label>
              <select
                value={videoRoom}
                onChange={e => setVideoRoom(e.target.value)}
                className="w-full border rounded-lg p-2 outline-none focus:ring-2 focus:ring-blue-500"
              >
                {ROOM_NAMES.map(r => <option key={r} value={r}>{r}</option>)}
              </select>
            </div>
          </div>
          <button
            type="submit"
            disabled={uploading}
            className="self-start bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg transition disabled:opacity-50"
          >
            Upload Video
          </button>
        </form>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
          {videos.length === 0 && <p className="text-gray-400 text-sm col-span-3">No videos uploaded yet</p>}
          {videos.map(video => (
            <div key={video.id} className="relative group bg-white rounded-xl shadow overflow-hidden">
              <video src={video.url} controls className="w-full h-40 object-cover" />
              <div className="p-2">
                <p className="text-sm font-medium truncate">{video.title}</p>
                <p className="text-xs text-blue-500">{video.room}</p>
              </div>
              <button
                onClick={() => { if (confirm('Delete this video?')) removeVideo(video.id) }}
                className="absolute top-2 right-2 bg-red-500 text-white rounded-full w-6 h-6 text-xs opacity-0 group-hover:opacity-100 transition"
              >
                ✕
              </button>
            </div>
          ))}
        </div>
      </section>
    </div>

    {/* Lightbox */}
    {lightboxUrl && (
      <div
        className="fixed inset-0 z-50 flex items-center justify-center bg-black/80 p-4"
        onClick={() => setLightboxUrl(null)}
      >
        <img
          src={lightboxUrl}
          alt={lightboxAlt}
          className="max-w-[90vw] max-h-[90vh] object-contain rounded shadow-2xl"
          onClick={e => e.stopPropagation()}
        />
        <button
          className="absolute top-4 right-4 text-white bg-black/50 hover:bg-black/80 rounded-full w-9 h-9 text-lg leading-none transition"
          onClick={() => setLightboxUrl(null)}
        >
          ✕
        </button>
      </div>
    )}
  )
}

export default MediaSection
