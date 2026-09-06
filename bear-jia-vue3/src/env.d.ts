/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_APP_TITLE?: string;
  readonly VITE_APP_ENV?: string;
  readonly VITE_APP_BASE_API?: string;
  /** @deprecated Use VITE_APP_BASE_API */
  readonly VITE_API_DOMAIN?: string;
  readonly VITE_API_BASE_URL?: string;
  readonly VITE_PORT?: string;
  readonly VITE_BASE_URL?: string;
  readonly VITE_PROXY_TARGET?: string;
  readonly VITE_VISUALIZER?: string;
  readonly VITE_VISUALIZER_OPEN?: string;
  readonly VITE_ANALYZE?: string;
  readonly VITE_ANALYZE_OPEN?: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}

