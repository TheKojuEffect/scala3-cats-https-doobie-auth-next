import {GetServerSideProps, NextPage} from "next";
import {User} from "../src/auth/User";

interface NewPageProps {
    user: User
}

export const getServerSideProps: GetServerSideProps<NewPageProps> = async ({req}) => {
    return await fetch(`${process.env.API_URL}/api/current-user`, {
        credentials: 'include',
        headers: {
            cookie: req.headers.cookie as string
        }
    })
        .then((response) => {
                if (response.ok) {
                    return response.json();
                }
                throw response;
            }
        )
        .then((user) => ({
                props: {
                    user
                }
            })
        ).catch(() => ({
                redirect: {
                    destination: '/',
                    permanent: false
                }
            })
        );
}

const New: NextPage<NewPageProps> = ({user}) => {
    return (<div>{user.id}</div>)
}

export default New;