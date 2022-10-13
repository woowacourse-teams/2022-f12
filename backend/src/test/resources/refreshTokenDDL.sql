create table if not exists refresh_token
(
    token_value varchar
(
    255
) primary key,
    expired_at datetime not null,
    member_id bigint not null
    );

truncate table refresh_token;
