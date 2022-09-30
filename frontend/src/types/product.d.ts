type Keyboard = { keyboard: '키보드' };
type Mouse = { mouse: '마우스' };
type Monitor = { monitor: '모니터' };
type Stand = { stand: '거치대' };
type Software = { software: '소프트웨어' };

type Categories = Keyboard & Mouse & Monitor & Stand & Software;
type Category = keyof Categories;

type Product = {
  id: number;
  name: string;
  imageUrl: string;
  reviewCount: number;
  rating: number;
  category: Category;
};

type Statistics = {
  careerLevel: {
    [Key in CareerLevel]: number;
  };
  jobType: {
    [Key in JobType]: number;
  };
};
