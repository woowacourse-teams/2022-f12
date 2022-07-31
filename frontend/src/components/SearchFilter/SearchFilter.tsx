import ChipFilter from '@/components/common/ChipFilter/ChipFilter';
import * as S from '@/components/SearchFilter/SearchFilter.style';

type Props = {
  careerLevelFilter: string;
  jobTypeFilter: string;
  handleCareerLevelFilterClick: (e) => void;
  handleJobTypeFilterClick: (e) => void;
};

const careerLevels = {
  none: '경력 없음',
  junior: '0-2년차',
  midlevel: '3-5년차',
  senior: '5년차 이상',
} as const;

const jobTypes = {
  frontend: '프론트엔드',
  backend: '백엔드',
  mobile: '모바일',
  etc: '기타',
} as const;

function SearchFilter({
  careerLevelFilter,
  jobTypeFilter,
  handleCareerLevelFilterClick,
  handleJobTypeFilterClick,
}: Props) {
  return (
    <S.Container>
      <S.Wrapper>
        <S.FilterTitle>경력</S.FilterTitle>
        {Object.entries(careerLevels).map(([value, content], index: number) => {
          return (
            <ChipFilter
              key={index}
              fontSize={14}
              value={value}
              careerLevelFilter={careerLevelFilter}
              handleClick={handleCareerLevelFilterClick}
            >
              {content}
            </ChipFilter>
          );
        })}
      </S.Wrapper>
      <S.Wrapper>
        <S.FilterTitle>직무</S.FilterTitle>
        {Object.entries(jobTypes).map(([value, content], index: number) => {
          return (
            <ChipFilter
              key={index}
              fontSize={14}
              value={value}
              jobTypeFilter={jobTypeFilter}
              handleClick={handleJobTypeFilterClick}
            >
              {content}
            </ChipFilter>
          );
        })}
      </S.Wrapper>
    </S.Container>
  );
}

export default SearchFilter;
