import { defineConfig } from 'vitepress'

const REPO = 'https://github.com/Nighterezi/HomeGUI'
const BASE = process.env.VITEPRESS_BASE || '/'

const enManualSidebar = [
  {
    text: 'Getting Started',
    items: [
      { text: 'Overview', link: '/docs/' },
      { text: 'Installation', link: '/docs/installation' },
      { text: 'FAQ', link: '/docs/faq' }
    ]
  },
  {
    text: 'Server Guide',
    items: [
      { text: 'Commands', link: '/docs/commands' },
      { text: 'Permissions', link: '/docs/permissions' },
      { text: 'Storage', link: '/docs/storage' }
    ]
  },
  {
    text: 'Configuration',
    items: [
      { text: 'Main Config', link: '/docs/configuration' },
      { text: 'Sounds', link: '/docs/sounds' },
      { text: 'Translations', link: '/docs/translations' }
    ]
  }
]

const viManualSidebar = [
  {
    text: 'Bắt đầu',
    items: [
      { text: 'Tổng quan', link: '/vi/docs/' },
      { text: 'Cài đặt', link: '/vi/docs/installation' },
      { text: 'Câu hỏi thường gặp', link: '/vi/docs/faq' }
    ]
  },
  {
    text: 'Hướng dẫn máy chủ',
    items: [
      { text: 'Lệnh', link: '/vi/docs/commands' },
      { text: 'Quyền', link: '/vi/docs/permissions' },
      { text: 'Lưu trữ', link: '/vi/docs/storage' }
    ]
  },
  {
    text: 'Cấu hình',
    items: [
      { text: 'Cấu hình chính', link: '/vi/docs/configuration' },
      { text: 'Âm thanh', link: '/vi/docs/sounds' },
      { text: 'Bản dịch', link: '/vi/docs/translations' }
    ]
  }
]

const enFeaturesSidebar = [
  {
    text: 'Features',
    items: [
      { text: 'Overview', link: '/features/' },
      { text: 'Home Screen', link: '/features/home-screen' },
      { text: 'Home Names', link: '/features/home-names' },
      { text: 'Teleport Rules', link: '/features/teleport-rules' }
    ]
  }
]

const viFeaturesSidebar = [
  {
    text: 'Tính năng',
    items: [
      { text: 'Tổng quan', link: '/vi/features/' },
      { text: 'Màn hình home', link: '/vi/features/home-screen' },
      { text: 'Tên home', link: '/vi/features/home-names' },
      { text: 'Luật dịch chuyển', link: '/vi/features/teleport-rules' }
    ]
  }
]

export default defineConfig({
  base: BASE,
  title: 'HomeGUI',
  description: 'Sethome, home and delhome for Fabric, with a screen instead of a wall of chat.',
  cleanUrls: true,
  lastUpdated: true,
  head: [
    ['link', { rel: 'icon', type: 'image/png', href: `${BASE}homegui-logo.png` }]
  ],
  // Notes for contributors, not a page on the site.
  srcExclude: ['CLAUDE.md'],
  themeConfig: {
    logo: '/homegui-logo.png',
    externalLinkIcon: true,
    socialLinks: [{ icon: 'github', link: REPO }],
    search: {
      provider: 'local'
    }
  },
  locales: {
    root: {
      label: 'English',
      lang: 'en',
      themeConfig: {
        nav: [
          { text: 'Home', link: '/', activeMatch: '^/$' },
          { text: 'Docs', link: '/docs/', activeMatch: '^/docs/' },
          { text: 'Features', link: '/features/', activeMatch: '^/features/' }
        ],
        sidebar: {
          '/features/': enFeaturesSidebar,
          '/docs/': enManualSidebar
        },
        editLink: {
          pattern: `${REPO}/edit/main/docs/:path`,
          text: 'Edit this page on GitHub'
        },
        outline: {
          level: [2, 3],
          label: 'On this page'
        },
        docFooter: {
          prev: 'Previous page',
          next: 'Next page'
        },
        lastUpdated: {
          text: 'Last updated',
          formatOptions: {
            dateStyle: 'medium',
            timeStyle: 'short'
          }
        }
      }
    },
    vi: {
      label: 'Tiếng Việt',
      lang: 'vi',
      description: 'Sethome, home và delhome cho Fabric, có giao diện thay vì một đống chữ trong chat.',
      themeConfig: {
        nav: [
          { text: 'Trang chủ', link: '/vi/', activeMatch: '^/vi/$' },
          { text: 'Tài liệu', link: '/vi/docs/', activeMatch: '^/vi/docs/' },
          { text: 'Tính năng', link: '/vi/features/', activeMatch: '^/vi/features/' }
        ],
        sidebar: {
          '/vi/features/': viFeaturesSidebar,
          '/vi/docs/': viManualSidebar
        },
        editLink: {
          pattern: `${REPO}/edit/main/docs/:path`,
          text: 'Chỉnh sửa trang này trên GitHub'
        },
        outline: {
          level: [2, 3],
          label: 'Trên trang này'
        },
        docFooter: {
          prev: 'Trang trước',
          next: 'Trang sau'
        },
        lastUpdated: {
          text: 'Cập nhật lần cuối',
          formatOptions: {
            dateStyle: 'medium',
            timeStyle: 'short'
          }
        },
        returnToTopLabel: 'Về đầu trang',
        sidebarMenuLabel: 'Menu',
        darkModeSwitchLabel: 'Giao diện',
        lightModeSwitchTitle: 'Chuyển sang giao diện sáng',
        darkModeSwitchTitle: 'Chuyển sang giao diện tối'
      }
    }
  }
})
