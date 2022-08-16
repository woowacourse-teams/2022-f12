declare type Senior = { senior: '6년차 이상' };
declare type Midlevel = { midlevel: '3-5년차' };
declare type Junior = { junior: '0-2년차' };
declare type NoCareer = { none: '경력없음' };

declare type CareerLevels = Senior & Midlevel & Junior & NoCareer;
declare type CareerLevel = keyof CareerLevels;

declare type Frontend = { frontend: '프론트엔드' };
declare type Backend = { backend: '백엔드' };
declare type Mobile = { mobile: '모바일' };
declare type Etc = { etc: '기타' };

declare type JobTypes = Frontend & Backend & Mobile & Etc;
declare type JobType = keyof JobTypes;

declare type Member = {
  id: number;
  gitHubId: string;
  imageUrl: string;
  name: string;
  careerLevel: CareerLevel;
  jobType: JobType;
};

declare type InventoryProduct = {
  id: number;
  selected: boolean;
  product: Product;
};

declare type ProfileSearchResult = Member & {
  profileProducts: Product[];
};
