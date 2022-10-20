type Senior = { senior: '6년차 이상' };
type Midlevel = { midlevel: '3-5년차' };
type Junior = { junior: '0-2년차' };
type NoCareer = { none: '경력 없음' };

type CareerLevels = Senior & Midlevel & Junior & NoCareer;
type CareerLevel = keyof CareerLevels;

type Frontend = { frontend: '프론트엔드' };
type Backend = { backend: '백엔드' };
type Mobile = { mobile: '모바일' };
type Etc = { etc: '기타' };

type JobTypes = Frontend & Backend & Mobile & Etc;
type JobType = keyof JobTypes;

type Member = {
  id: number;
  gitHubId: string;
  imageUrl: string;
  name: string;
  careerLevel: CareerLevel;
  jobType: JobType;
  followerCount: number;
  following: boolean;
};

type InventoryProduct = {
  id: number;
  selected: boolean;
  product: Product;
};

type ProfileSearchResult = Member & {
  profileProducts: Product[];
};
