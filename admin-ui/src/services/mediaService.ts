import api from './api'
import type { Photo, Video } from '../types'

const CLOUD_NAME = import.meta.env.VITE_CLOUDINARY_CLOUD_NAME
const UPLOAD_PRESET = import.meta.env.VITE_CLOUDINARY_UPLOAD_PRESET

// sends the file to Cloudinary and returns the public URL — I use Cloudinary because S3 free tier only lasts 12 months
export async function uploadToCloudinary(file: File): Promise<string> {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('upload_preset', UPLOAD_PRESET)

  const response = await fetch(`https://api.cloudinary.com/v1_1/${CLOUD_NAME}/upload`, {
    method: 'POST',
    body: formData,
  })

  const data = await response.json()
  if (!response.ok) throw new Error(data.error?.message || 'Upload failed')
  return data.secure_url
}

// --- PHOTOS ---

export async function getPhotos(): Promise<Photo[]> {
  const response = await api.get('/api/media/photos')
  return response.data
}

export async function createPhoto(photo: Omit<Photo, 'uploadedAt'>): Promise<Photo> {
  const response = await api.post('/api/media/photos', photo)
  return response.data
}

export async function deletePhoto(id: string): Promise<void> {
  await api.delete(`/api/media/photos/${id}`)
}

// --- VIDEOS ---

export async function getVideos(): Promise<Video[]> {
  const response = await api.get('/api/media/videos')
  return response.data
}

export async function createVideo(video: Omit<Video, 'uploadedAt'>): Promise<Video> {
  const response = await api.post('/api/media/videos', video)
  return response.data
}

export async function deleteVideo(id: string): Promise<void> {
  await api.delete(`/api/media/videos/${id}`)
}
