declare module '*.jpg' {
  const value: string;
  export = value;
}

declare module '*.svg' {
  const value: React.FC<{ fill?: string; stroke?: string }>;
  export = value;
}

declare type Member = {
  id: number;
  gitHubId: string;
  imageUrl: string;
  name: string;
};

declare type UserData = {
  member: Member;
  token: string;
  registerCompleted: boolean;
};

declare type Product = {
  id: number;
  name: string;
  imageUrl: string;
  rating: number;
  reviewCount: number;
  category: string;
};

declare type InventoryProduct = {
  id: number;
  selected: boolean;
  product: Product;
};

declare type Review = {
  id: number;
  author: {
    id: number;
    gitHubId: string;
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

declare type ProfileSearchResult = {
  id: number;
  gitHubId: string;
  name: string;
  imageUrl: string;
  careerLevel: string;
  jobType: string;
  profileProducts: [
    {
      id: number;
      name: string;
      imageUrl: string;
      reviewCount: number;
      rating: number;
      category: string;
    }
  ];
};

declare type Statistics = {
  careerLevel: {
    midlevel: number;
    senior: number;
    none: number;
    junior: number;
  };
  jobType: {
    frontend: number;
    backend: number;
    mobile: number;
    etc: number;
  };
};

declare const __API_URL__: string;
declare const __GITHUB_CLIENT_ID__: string;
