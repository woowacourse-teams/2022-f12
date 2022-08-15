declare type Keyboard = { keyboard: '키보드' };
declare type Mouse = { mouse: '마우스' };
declare type Monitor = { monitor: '모니터' };
declare type Stand = { stand: '거치대' };
declare type Software = { software: '소프트웨어' };

declare type Categories = Keyboard & Mouse & Monitor & Stand & Software;
declare type Category = keyof Categories;

declare type Product = {
  id: number;
  name: string;
  imageUrl: string;
  reviewCount: number;
  rating: number;
  category: Category;
};

declare type Statistics = {
  careerLevel: {
    [Key in CareerLevel]: number;
  };
  jobType: {
    [Key in JobType]: number;
  };
};
