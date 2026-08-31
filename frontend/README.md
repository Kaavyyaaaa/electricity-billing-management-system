# ElectraCare Customer Portal

A professional **Angular 18 frontend-only** demo for customer registration, dashboard, bill viewing, and bill summary.

## Important

- No backend is included.
- No HTTP/API requests are made.
- Registration and bill selections use browser `localStorage`.
- Payment, support, complaint, and history are visual placeholders only.

## Requirements

- Node.js 18.19.1+ (Node.js 20 LTS is recommended for Angular 18)
- npm
- Angular CLI 18

## Run locally

```bash
npm install -g @angular/cli@18
cd electricity-customer-portal
npm install
npm start
```

Open `http://localhost:4200`.

## Production build

```bash
npm run build
```

The build output is created under `dist/`.
