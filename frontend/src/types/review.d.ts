type Review = {
  id: number;
  author: Pick<Member, 'id' | 'gitHubId' | 'imageUrl'>;
  product?: Pick<Product, 'id' | 'name' | 'imageUrl'>;
  productId?: Product['id'];
  content: string;
  rating: number;
  createdAt: string;
  authorMatch: boolean;
};

type ReviewInput = {
  content: string;
  rating: number;
};

type InventoryReview = {
  id: number;
  product: Product;
  content: string;
  rating: number;
  createdAt: string;
};
