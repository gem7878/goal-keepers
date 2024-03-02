/** @type {import('next').NextConfig} */
const nextConfig = {
  async rewrites() {
    return [
      {
        source: '/:path*',
        destination: `${process.env.NEXT_PUBLIC_API_URL}/:path*`,
      },
    ];
  },
  reactStrictMode: false,
  images: {
    domains: ['storage.googleapis.com'],
  },
};

module.exports = nextConfig;
