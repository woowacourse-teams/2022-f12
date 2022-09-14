declare module '*.svg' {
  const value: React.FC<{ fill?: string; stroke?: string }>;
  export = value;
}

declare module '*.gif' {
  const value: string;
  export = value;
}

declare module '*.webp' {
  const value: string;
  export = value;
}
