declare module '*.jpg' {
  const value: string;
  export = value;
}

declare module '*.svg' {
  const value: React.FC;
  export = value;
}

declare type Product = {
  id: number;
  name: string;
  imageUrl: string;
  rating: number;
};

declare type Review = {
  id: number;
  profileImage: string;
  username: string;
  rating: number;
  content: string;
};

declare type ReviewInput = {
  content: string;
  rating: number;
};
