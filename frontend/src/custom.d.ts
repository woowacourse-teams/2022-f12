declare module '*.jpg' {
  const value: string;
  export = value;
}

declare module '*.svg' {
  const value: React.FC<{ fill?: string; stroke?: string }>;
  export = value;
}

declare type UserData = {
  token: string;
  jobType: string;
  career: string;
};

declare type Product = {
  id: number;
  name: string;
  imageUrl: string;
  rating: number;
};

declare type Review = {
  id: number;
  author: {
    id: number;
    name: string;
    imageUrl: string;
  };
  product?: {
    id: number;
    name: string;
    imageUrl: string;
  };
  productId?: number;
  content: string;
  rating: number;
  createdAt: string;
};

declare type ReviewInput = {
  content: string;
  rating: number;
};
