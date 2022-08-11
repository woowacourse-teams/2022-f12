import * as S from '@/pages/Register/Register.style';

type Props = {
  careerLevel: string;
  jobType: string;
  handleEdit: () => void;
  handleConfirm: () => void;
};

function ConfirmInfoForm({ careerLevel, jobType, handleEdit, handleConfirm }: Props) {
  return (
    <>
      <S.ConfirmInfo>{careerLevel}</S.ConfirmInfo>
      <S.ConfirmInfo>{jobType}</S.ConfirmInfo>
      <S.FlexRowWrapper>
        <S.EditButton onClick={handleEdit}>수정하기</S.EditButton>
        <S.ConfirmButton onClick={handleConfirm}>제출하기</S.ConfirmButton>
      </S.FlexRowWrapper>
    </>
  );
}

export default ConfirmInfoForm;
