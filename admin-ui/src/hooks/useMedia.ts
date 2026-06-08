import { useState, useEffect } from 'react'
import type { Photo, Video } from '../types'
import {
  getPhotos, createPhoto, deletePhoto,
  getVideos, createVideo, deleteVideo,
  uploadToCloudinary
} from '../services/mediaService'
import { v4 as uuidv4 } from 'uuid'

function useMedia() {
  const [photos, setPhotos] = useState<Photo[]>([])
  const [videos, setVideos] = useState<Video[]>([])
  const [loading, setLoading] = useState(true)
  const [uploading, setUploading] = useState(false)
  const [error, setError] = useState('')

  useEffect(() => {
    async function fetchMedia() {
      try {
        const [photosData, videosData] = await Promise.all([getPhotos(), getVideos()])
        setPhotos(photosData)
        setVideos(videosData)
      } catch {
        setError('Failed to load media')
      } finally {
        setLoading(false)
      }
    }
    fetchMedia()
  }, [])

  async function addPhoto(file: File, altText: string, room: string) {
    try {
      setUploading(true)
      const url = await uploadToCloudinary(file)
      const photo = await createPhoto({ id: uuidv4(), url, altText, room })
      setPhotos(prev => [...prev, photo])
    } catch {
      setError('Failed to upload photo')
    } finally {
      setUploading(false)
    }
  }

  async function removePhoto(id: string) {
    try {
      await deletePhoto(id)
      setPhotos(prev => prev.filter(p => p.id !== id))
    } catch {
      setError('Failed to delete photo')
    }
  }

  async function addVideo(file: File, altText: string, title: string, room: string) {
    try {
      setUploading(true)
      const url = await uploadToCloudinary(file)
      const video = await createVideo({ id: uuidv4(), url, altText, title, room })
      setVideos(prev => [...prev, video])
    } catch {
      setError('Failed to upload video')
    } finally {
      setUploading(false)
    }
  }

  async function removeVideo(id: string) {
    try {
      await deleteVideo(id)
      setVideos(prev => prev.filter(v => v.id !== id))
    } catch {
      setError('Failed to delete video')
    }
  }

  return { photos, videos, loading, uploading, error, addPhoto, removePhoto, addVideo, removeVideo }
}

export default useMedia
