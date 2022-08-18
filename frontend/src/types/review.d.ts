declare type Review = {
  id: number;
  author: Pick<Member, 'id' | 'gitHubId' | 'imageUrl'>;
  product?: Pick<Product, 'id' | 'name' | 'imageUrl'>;
  productId?: Product['id'];
  content: string;
  rating: number;
  createdAt: string;
  authorMatch: boolean;
};

declare type ReviewInput = {
  content: string;
  rating: number;
};
