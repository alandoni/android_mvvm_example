import express, { json } from 'express';
import { networkInterfaces } from 'os';

const app = express();
const port = 8080;

const users = [{
    remoteId: '3',
    name: 'User',
    password: 'Pass',
    image: 'image'
}];

const posts = [];
const comments = [];

app.use(json());

app.use((request, response, next) => {
    setTimeout(() => {
        next();
    }, 2000);
});

app.post('/login', (request, response) => {
    console.log(request.body);
    const username = request.body.username;
    const password = request.body.password;

    console.log(`User sent: ${username} and ${password}`);

    const user = users.find((u) => {
        return u.name === username
    });
    if (!user) {
        response.status(404).send('Unauthorized');
        return
    }
    if (user.password != password) {
        response.status(401).send('Unauthorized');
        return
    }
    response.send(user);
});

app.post('/post', (request, response) => {
    const post = request.body;
    post.status = 'RECEIVED';

    console.log(`User sent post: ${JSON.stringify(post)}`);

    const postIndex = posts.findIndex((p) => {
        return p.id === post.id;
    });

    post.date = new Date().getTime();

    if (postIndex === -1) {
        posts.push(post);
    } else {
        posts[postIndex] = post;
    }

    response.send(post);
});

app.get('/post', (_request, response) => {
    console.log('User requested all posts');
    console.log(JSON.stringify(posts));
    response.send(posts);
});

app.post('/post/:id/comment', (request, response) => {
    const postId = request.params.id;
    console.log(`User commented to post: ${postId}`);

    const post = posts.find((post) => {
        return post.id === id;
    });

    if (!post) {
        response.status(404).send('Not found');
        return
    }

    const comment = request.body;
    console.log(`User commented to post ${postId}: ${JSON.stringify(comment)}`);
    comment.postId = postId;
    comments.push(comment)

    response.send(comment);
});

app.get('/post/:id/comment', (request, response) => {
    const postId = request.params.id;

    console.log(`User requested comments of post: ${postId}`);

    const post = posts.find((post) => {
        return post.id === id;
    });

    if (!post) {
        response.status(404).send('Not found');
        return
    }

    const commentsOfPost = comments.filter((c) => {
        return c.postId === postId;
    });
    response.send(commentsOfPost);
});

app.listen(port, () => {
    const nets = networkInterfaces().en0.filter((n) => {
        return n.family === 'IPv4';
    })[0];

    console.log(`Servidor iniciado no endereço: ${nets.address}:${port}`);
});
