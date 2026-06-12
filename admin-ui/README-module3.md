# Overview

As a software engineer, I wanted to close the gap between the backend I built in the last two modules and something a real person can actually use. I had a fully working REST API connected to AWS DynamoDB, but the only way to interact with it was through Bruno making raw HTTP calls. That is not something I could hand to a hotel owner. This module was about building the frontend that turns all those API endpoints into a real admin panel.

The software I built is a React admin panel for Hostería Maribao, a hotel business I have been building software for over the past year. It connects directly to the Spring Boot API from modules 1 and 2 and lets the hotel owner manage everything from one place — view and update guest reservations, configure discount dates for the booking calendar, upload photos and videos to the website gallery through Cloudinary, and track visit statistics. To run it locally, start the Spring Boot API first on port 8080, then run `pnpm dev` inside the `admin-ui` folder. The app will be available at `http://localhost:5173`.

I built this because the backend work only matters if someone can use it. The hotel owner does not know what a REST API is — they need a screen with buttons. This module gave me a reason to learn React in a real context instead of a tutorial counter app, and it produced something I can actually deploy and hand over to the client.

[Software Demo Video](https://youtu.be/b6CdggJ36J0)

# Web Pages

**Login Page (`/login`)**
This is the first page that loads when you open the app. It has a username and password form. The content is dynamic — if the credentials are wrong, an error message appears below the form without reloading the page. When the credentials are correct, React Router redirects the user to the dashboard and stores the session in localStorage so the login persists across page refreshes. If a logged-out user tries to go directly to `/`, the app redirects them back to `/login` automatically.

**Dashboard (`/`)**
This is where all the hotel management happens. It is a single page with a sidebar that switches between four sections — each one is a separate React component that loads its own data from the API:

- **Reservations** — shows a table of all guest reservations pulled from DynamoDB. Each row has action buttons to change the status (pending, confirmed, cancelled) or delete the reservation. The status badge updates live without a page reload when the user clicks a button.
- **Discounts** — shows all discount records and lets the owner add new ones through a form that slides open on the page. Each discount can be toggled active or inactive, which controls whether it shows on the booking calendar.
- **Media** — lets the owner upload photos and videos directly from the browser. Files go to Cloudinary and the returned URL is saved to DynamoDB through the API. Uploaded media shows in a grid below the upload form.
- **Stats** — shows total visit count and today's count as cards at the top. Below that, a month selector lets the owner pick any month and see a weekly breakdown of visit traffic for that period.

# Development Environment

I used IntelliJ IDEA as the editor for this module, the same as in the previous two modules. Bruno was still used to verify the Spring Boot API endpoints during development. Git and GitHub were used for source control.

The project is written in TypeScript using React 19 as the UI framework and Vite as the build tool and development server. Vite replaces the old Create React App setup — it is significantly faster and has native TypeScript support out of the box. The following libraries were used:

- React Router v7 — handles client-side routing between `/login` and `/`. I used `BrowserRouter` with a `PrivateRoute` wrapper to protect the dashboard from unauthenticated access
- Tailwind CSS v4 — utility-first CSS framework for all the styling. I used the new Vite plugin approach (`@tailwindcss/vite`) instead of the old PostCSS config because Tailwind v4 changed how it integrates
- Axios — HTTP client for all API calls to the Spring Boot backend. I set up a single instance with the base URL so I do not repeat it in every service file
- Cloudinary — handles media uploads directly from the browser using an unsigned upload preset. I chose it over S3 because Cloudinary's free tier does not expire after 12 months
- uuid — generates unique IDs on the client side before sending new records to the API

# New Files for This Module

The `admin-ui` directory was created entirely for this module — it is the React application. Every subdirectory and file inside it (components, hooks, services, pages, context, types, utils, etc.) was created as part of module 3.

# Useful Websites

- [React Documentation](https://react.dev)
- [Vite Documentation](https://vite.dev)
- [Tailwind CSS v4 Documentation](https://tailwindcss.com/docs)
- [React Router Documentation](https://reactrouter.com)
- [Cloudinary Upload API](https://cloudinary.com/documentation/upload_images)
- [Axios Documentation](https://axios-http.com/docs/intro)
- [TypeScript Handbook](https://www.typescriptlang.org/docs/handbook/intro.html)

# Future Work

- Deploy the admin panel to AWS S3 + CloudFront so it is accessible from anywhere without running Vite locally
- Replace the hardcoded username and password with real backend authentication — right now credentials are stored in the frontend code which is not secure
- Wire the public maribao.com website to the Spring Boot API so the booking calendar, gallery, and discount prices all come from the same database the admin panel writes to
- Add email notifications through Resend when a reservation is created or cancelled so the owner gets alerted without checking the panel
- Add PDF generation per reservation so the owner can print or email a confirmation to guests
