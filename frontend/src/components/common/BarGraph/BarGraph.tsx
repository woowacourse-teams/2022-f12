import { useState } from 'react';

import * as S from '@/components/common/BarGraph/BarGraph.style';

import { SROnly } from '@/style/GlobalStyles';
import theme from '@/style/theme';

type Prop = {
  statistics: Statistics;
};

type JobType = ['프론트엔드', '백엔드', '모바일', '기타'];
type CareerLevel = ['경력 없음', '0-2년차', '3-5년차', '6년차 이상'];

const jobTypeList: JobType = ['프론트엔드', '백엔드', '모바일', '기타'];
const careerLevelList: CareerLevel = ['경력 없음', '0-2년차', '3-5년차', '6년차 이상'];

function BarGraph({ statistics }: Prop) {
  const [isJobType, setIsJobType] = useState(true);
  const { careerLevel, jobType } = statistics;

  const statisticsData = [
    {
      color: theme.colors.primary,
      percent: Math.round((isJobType ? jobType.frontend : careerLevel.none) * 100),
      label: isJobType ? jobTypeList[0] : careerLevelList[0],
    },
    {
      color: theme.colors.primaryDark,
      percent: Math.round((isJobType ? jobType.backend : careerLevel.junior) * 100),
      label: isJobType ? jobTypeList[1] : careerLevelList[1],
    },
    {
      color: theme.colors.secondary,
      percent: Math.round((isJobType ? jobType.mobile : careerLevel.midlevel) * 100),
      label: isJobType ? jobTypeList[2] : careerLevelList[2],
    },
    {
      color: theme.colors.black,
      percent: Math.round((isJobType ? jobType.etc : careerLevel.senior) * 100),
      label: isJobType ? jobTypeList[3] : careerLevelList[3],
    },
  ];

  const toggleGraph = () => {
    setIsJobType((state) => !state);
  };

  return (
    <S.Container aria-label="통계 정보">
      <S.BarGraphTitleWrapper>
        <S.BarGraphTitle>{isJobType ? '직군별 통계' : '연차별 통계'}</S.BarGraphTitle>
        <S.BarGraphToggleButton onClick={toggleGraph}>
          <span aria-hidden={'true'}>
            {isJobType ? '연차별 통계 보기' : '직군별 통계 보기'}
          </span>
          <SROnly>
            {isJobType
              ? '연차별 통계 보기 버튼. 연차별 통계를 보려면 클릭하시고, 직군별 통계를 보려면 다음으로 탭 하세요.'
              : '직군별 통계 보기 버튼. 직군별 통계를 보려면 클릭하시고, 연차별 통계를 보려면 다음으로 탭 하세요.'}
          </SROnly>
        </S.BarGraphToggleButton>
      </S.BarGraphTitleWrapper>
      <S.DataWrapper>
        {statisticsData.map((data, index) => {
          return (
            <S.BarWrapper key={index}>
              <S.Bar key={Math.random()} color={data.color} height={data.percent} />
              <S.PercentWrapper>
                <S.Percent aria-hidden="true">{`${data.percent}%`}</S.Percent>
                <SROnly>{`전체 ${isJobType ? '직군' : '연차'} 중 ${data.percent}%의 ${
                  data.label
                } 개발자가 이 제품을 사용하고 있습니다.`}</SROnly>
              </S.PercentWrapper>
            </S.BarWrapper>
          );
        })}
      </S.DataWrapper>
      <S.JobTypeWrapper aria-hidden="true">
        {isJobType
          ? jobTypeList.map((title, index) => {
              return <S.JobType key={index}>{title}</S.JobType>;
            })
          : careerLevelList.map((title, index) => {
              return <S.JobType key={index}>{title}</S.JobType>;
            })}
      </S.JobTypeWrapper>
    </S.Container>
  );
}
export default BarGraph;
