// mirrors the Reservation Java model — field names have to match exactly or JSON deserialization breaks
export interface Reservation {
  id: string
  guestName: string
  email: string
  phone: string
  roomName: string
  checkIn: string
  checkOut: string
  totalPrice: number
  status: string
  comment?: string
}

// mirrors the Discount Java model
export interface Discount {
  id: string
  date: string
  percentage: number
  description: string
  active: boolean
}

// mirrors the Photo Java model — room tells me which hotel room this photo belongs to
export interface Photo {
  id: string
  url: string
  altText: string
  room: string
  uploadedAt: string
}

// mirrors the Video Java model — room tells me which hotel room this video belongs to
export interface Video {
  id: string
  url: string
  altText: string
  title: string
  room: string
  uploadedAt: string
}
