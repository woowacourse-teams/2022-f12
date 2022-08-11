type Props = {
  data: Review[];
  handleDelete: (id: number) => void;
  handleEdit: (reviewInput: ReviewInput, id: number) => Promise<void>;
  loginUserGithubId: string;
};

function ReviewList({ data, handleDelete, handleEdit, loginUserGithubId }: Props) {
  return <></>;
}

export default ReviewList;
