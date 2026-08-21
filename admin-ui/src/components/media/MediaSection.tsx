import { useState, useEffect } from 'react'
import useMedia from '../../hooks/useMedia'
import { ROOM_NAMES } from '../../constants'

function MediaSection() {
  const { photos, videos, loading, uploading, error, addPhoto, removePhoto, addVideo, removeVideo } = useMedia()

  const [lightboxUrl, setLightboxUrl] = useState<string | null>(null)
  const [lightboxAlt, setLightboxAlt] = useState('')

  // confirmation modal state
  const [pendingDelete, setPendingDelete] = useState<{ id: string; type: 'photo' | 'video'; preview: string } | null>(null)

  useEffect(() => {
    function onKey(e: KeyboardEvent) {
      if (e.key === 'Escape') {
        setLightboxUrl(null)
        setPendingDelete(null)
      }
    }
    window.addEventListener('keydown', onKey)
    return () => window.removeEventListener('keydown', onKey)
  }, [])

  function confirmDelete() {
    if (!pendingDelete) return
    if (pendingDelete.type === 'photo') removePhoto(pendingDelete.id)
    else removeVideo(pendingDelete.id)
    setPendingDelete(null)
  }

  // photo upload form state
  const [photoFile, setPhotoFile] = useState<File | null>(null)
  const [photoRoom, setPhotoRoom] = useState(ROOM_NAMES[0])

  // video upload form state
  const [videoFile, setVideoFile] = useState<File | null>(null)
  const [videoRoom, setVideoRoom] = useState(ROOM_NAMES[0])

  async function handlePhotoSubmit(e: React.FormEvent) {
    e.preventDefault()
    if (!photoFile) return
    await addPhoto(photoFile, photoRoom, photoRoom)
    setPhotoFile(null)
    setPhotoRoom(ROOM_NAMES[0])
  }

  async function handleVideoSubmit(e: React.FormEvent) {
    e.preventDefault()
    if (!videoFile) return
    await addVideo(videoFile, videoRoom, videoRoom, videoRoom)
    setVideoFile(null)
    setVideoRoom(ROOM_NAMES[0])
  }

  if (loading) return <p className="text-gray-500">Loading media...</p>
  if (error) return <p className="text-red-500">{error}</p>

  return (
    <>
      <div className="space-y-10">
        <h2 className="text-2xl font-bold">Media</h2>

        {uploading && (
          <div className="bg-blue-50 text-blue-600 px-4 py-3 rounded-lg text-sm font-medium">
            Uploading to Cloudinary...
          </div>
        )}

        {/* PHOTOS */}
        <section>
          <h3 className="text-lg font-semibold mb-4">Photos</h3>

          <form onSubmit={handlePhotoSubmit} className="bg-white p-4 desk:p-6 rounded-xl shadow mb-6 flex flex-col gap-4">
            {/* Photo file picker */}
            <input
              id="photo-input"
              type="file"
              accept="image/*"
              onChange={e => setPhotoFile(e.target.files?.[0] || null)}
              className="hidden"
              required
            />
            <label
              htmlFor="photo-input"
              className={`flex flex-col items-center justify-center gap-2 border-2 border-dashed rounded-xl p-6 cursor-pointer transition touch-manipulation
                ${photoFile ? 'border-blue-400 bg-blue-50' : 'border-gray-300 bg-gray-50 hover:border-blue-400 hover:bg-blue-50'}`}
            >
              {photoFile ? (
                <>
                  <img
                    src={URL.createObjectURL(photoFile)}
                    alt="Preview"
                    className="w-24 h-24 object-cover rounded-lg shadow"
                  />
                  <p className="text-sm font-medium text-blue-600 text-center break-all">{photoFile.name}</p>
                  <p className="text-xs text-gray-400">Tap to change</p>
                </>
              ) : (
                <>
                  <svg className="w-10 h-10 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                  </svg>
                  <p className="text-sm font-medium text-gray-600">Tap to choose a photo</p>
                  <p className="text-xs text-gray-400">JPG, PNG, WEBP, etc.</p>
                </>
              )}
            </label>

            <div>
              <label className="block text-sm text-gray-600 mb-1">Room</label>
              <select
                value={photoRoom}
                onChange={e => setPhotoRoom(e.target.value)}
                className="w-full border rounded-lg p-2.5 outline-none focus:ring-2 focus:ring-blue-500"
              >
                {ROOM_NAMES.map(r => <option key={r} value={r}>{r}</option>)}
              </select>
            </div>
            <button
              type="submit"
              disabled={uploading || !photoFile}
              className="w-full desk:w-auto desk:self-start bg-blue-600 hover:bg-blue-700 text-white px-6 py-2.5 rounded-lg transition font-medium disabled:opacity-50"
            >
              Upload Photo
            </button>
          </form>

          <div className="grid grid-cols-2 desk:grid-cols-3 lg:grid-cols-4 gap-3 desk:gap-4">
            {photos.length === 0 && <p className="text-gray-400 text-sm col-span-2 desk:col-span-4">No photos uploaded yet</p>}
            {photos.map(photo => (
              <div key={photo.id} className="relative group bg-white rounded-xl shadow overflow-hidden">
                <img
                  src={photo.url}
                  alt={photo.altText}
                  className="w-full h-32 desk:h-40 object-cover cursor-zoom-in active:opacity-80 transition"
                  onClick={() => { setLightboxUrl(photo.url); setLightboxAlt(photo.altText) }}
                />
                <div className="p-2">
                  <p className="text-xs text-gray-500 truncate">{photo.altText}</p>
                  <p className="text-xs text-blue-500">{photo.room}</p>
                </div>
                <button
                  onClick={() => setPendingDelete({ id: photo.id, type: 'photo', preview: photo.url })}
                  className="absolute top-2 right-2 bg-red-500 text-white rounded-full w-7 h-7 text-xs flex items-center justify-center transition touch-manipulation"
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

          <form onSubmit={handleVideoSubmit} className="bg-white p-4 desk:p-6 rounded-xl shadow mb-6 flex flex-col gap-4">
            {/* Video file picker */}
            <input
              id="video-input"
              type="file"
              accept="video/*"
              onChange={e => setVideoFile(e.target.files?.[0] || null)}
              className="hidden"
              required
            />
            <label
              htmlFor="video-input"
              className={`flex flex-col items-center justify-center gap-2 border-2 border-dashed rounded-xl p-6 cursor-pointer transition touch-manipulation
                ${videoFile ? 'border-purple-400 bg-purple-50' : 'border-gray-300 bg-gray-50 hover:border-purple-400 hover:bg-purple-50'}`}
            >
              {videoFile ? (
                <>
                  <svg className="w-10 h-10 text-purple-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M15 10l4.553-2.276A1 1 0 0121 8.723v6.554a1 1 0 01-1.447.894L15 14M3 8a2 2 0 012-2h8a2 2 0 012 2v8a2 2 0 01-2 2H5a2 2 0 01-2-2V8z" />
                  </svg>
                  <p className="text-sm font-medium text-purple-600 text-center break-all">{videoFile.name}</p>
                  <p className="text-xs text-gray-400">Tap to change</p>
                </>
              ) : (
                <>
                  <svg className="w-10 h-10 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M15 10l4.553-2.276A1 1 0 0121 8.723v6.554a1 1 0 01-1.447.894L15 14M3 8a2 2 0 012-2h8a2 2 0 012 2v8a2 2 0 01-2 2H5a2 2 0 01-2-2V8z" />
                  </svg>
                  <p className="text-sm font-medium text-gray-600">Tap to choose a video</p>
                  <p className="text-xs text-gray-400">MP4, MOV, WEBM, etc.</p>
                </>
              )}
            </label>

            <div>
              <label className="block text-sm text-gray-600 mb-1">Room</label>
              <select
                value={videoRoom}
                onChange={e => setVideoRoom(e.target.value)}
                className="w-full border rounded-lg p-2.5 outline-none focus:ring-2 focus:ring-blue-500"
              >
                {ROOM_NAMES.map(r => <option key={r} value={r}>{r}</option>)}
              </select>
            </div>
            <button
              type="submit"
              disabled={uploading || !videoFile}
              className="w-full desk:w-auto desk:self-start bg-blue-600 hover:bg-blue-700 text-white px-6 py-2.5 rounded-lg transition font-medium disabled:opacity-50"
            >
              Upload Video
            </button>
          </form>

          <div className="grid grid-cols-1 desk:grid-cols-2 lg:grid-cols-3 gap-3 desk:gap-4">
            {videos.length === 0 && <p className="text-gray-400 text-sm col-span-3">No videos uploaded yet</p>}
            {videos.map(video => (
              <div key={video.id} className="relative group bg-white rounded-xl shadow overflow-hidden">
                <video src={video.url} controls className="w-full h-40 object-cover" />
                <div className="p-2">
                  <p className="text-sm font-medium truncate">{video.title}</p>
                  <p className="text-xs text-blue-500">{video.room}</p>
                </div>
                <button
                  onClick={() => setPendingDelete({ id: video.id, type: 'video', preview: video.url })}
                  className="absolute top-2 right-2 bg-red-500 text-white rounded-full w-7 h-7 text-xs flex items-center justify-center transition touch-manipulation"
                >
                  ✕
                </button>
              </div>
            ))}
          </div>
        </section>
      </div>

      {/* Delete confirmation modal */}
      {pendingDelete && (
        <div
          className="fixed inset-0 z-50 flex items-center justify-center bg-black/60 p-4"
          onClick={() => setPendingDelete(null)}
        >
          <div
            className="bg-white rounded-2xl shadow-2xl w-full max-w-sm p-6 flex flex-col items-center gap-4"
            onClick={e => e.stopPropagation()}
          >
            {pendingDelete.type === 'photo' ? (
              <img
                src={pendingDelete.preview}
                alt="To be deleted"
                className="w-full h-40 object-cover rounded-xl"
              />
            ) : (
              <video
                src={pendingDelete.preview}
                className="w-full h-40 object-cover rounded-xl"
              />
            )}
            <div className="text-center">
              <p className="font-semibold text-gray-800 text-lg">Delete this {pendingDelete.type}?</p>
              <p className="text-sm text-gray-500 mt-1">This action cannot be undone.</p>
            </div>
            <div className="flex gap-3 w-full">
              <button
                onClick={() => setPendingDelete(null)}
                className="flex-1 bg-gray-100 hover:bg-gray-200 active:bg-gray-300 text-gray-700 font-medium py-2.5 rounded-xl transition touch-manipulation"
              >
                Cancel
              </button>
              <button
                onClick={confirmDelete}
                className="flex-1 bg-red-500 hover:bg-red-600 active:bg-red-700 text-white font-medium py-2.5 rounded-xl transition touch-manipulation"
              >
                Delete
              </button>
            </div>
          </div>
        </div>
      )}

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
            className="absolute top-4 right-4 text-white bg-black/50 hover:bg-black/80 rounded-full w-10 h-10 text-lg flex items-center justify-center transition"
            onClick={() => setLightboxUrl(null)}
          >
            ✕
          </button>
        </div>
      )}
    </>
  )
}

export default MediaSection
