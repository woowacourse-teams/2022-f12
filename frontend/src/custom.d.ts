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

declare type InventoryProduct = {
  id: number;
  selected: boolean;
  product: Product;
};

declare type Review = {
  id: number;
  product?: {
    id: number;
    name: string;
    imageUrl: string;
  };
  profileImage: string;
  username: string;
  rating: number;
  content: string;
  createdAt: string;
};

declare type ReviewInput = {
  content: string;
  rating: number;
};
