
export interface PointData {
    tool: string;
    points: number[];
}

// {
//   "room_id": 1,
//     "user_list": [
//       { "user_id": 1, "user_role": "drawer", "is_liar": true },
//       { "user_id": 2, "user_role": "watcher", "is_liar": true },
//       { "user_id": 3, "user_role": "watcher", "is_liar": true },
//       { "user_id": 4, "user_role": "watcher", "is_liar": true }
//   ]
// }

export interface User {
    user_id: number;
    user_role: "drawer" | "watcher"; // 사용자 역할을 문자열 리터럴 타입으로 제한
    is_liar: boolean;
}

export interface Game1Data {
    room_id: number;
    user_list: User[];
}


