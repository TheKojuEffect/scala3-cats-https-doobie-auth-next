/** @type {import('next').NextConfig} */
module.exports = {
    reactStrictMode: true,
    async rewrites() {
        return {
            beforeFiles: [
                {
                    source: '/api/:path*',
                    destination: 'http://localhost:9000/:path*',
                    basePath: false
                }
            ]
        }
    }
};
