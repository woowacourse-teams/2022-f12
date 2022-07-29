import * as S from '@/components/common/BarGraph/BarGraph.style';
import theme from '@/style/theme';

function BarGraph() {
  return (
    <S.Container>
      <S.DataWrapper>
        <S.BarWrapper>
          <S.Bar color={theme.colors.secondary} height={25} />
          <S.PercentWrapper>
            <S.Percent>25%</S.Percent>
          </S.PercentWrapper>
        </S.BarWrapper>
        <S.BarWrapper>
          <S.Bar color={theme.colors.black} height={50} />
          <S.PercentWrapper>
            <S.Percent>50%</S.Percent>
          </S.PercentWrapper>
        </S.BarWrapper>
        <S.BarWrapper>
          <S.Bar color={theme.colors.primary} height={75} />
          <S.PercentWrapper>
            <S.Percent>75%</S.Percent>
          </S.PercentWrapper>
        </S.BarWrapper>
        <S.BarWrapper>
          <S.Bar color={theme.colors.primaryDark} height={100} />
          <S.PercentWrapper>
            <S.Percent>100%</S.Percent>
          </S.PercentWrapper>
        </S.BarWrapper>
      </S.DataWrapper>
      <S.JobTypeWrapper>
        <S.JobType>프론트엔드</S.JobType>
        <S.JobType>백엔드</S.JobType>
        <S.JobType>모바일</S.JobType>
        <S.JobType>기타</S.JobType>
      </S.JobTypeWrapper>
    </S.Container>
  );
}

export default BarGraph;
