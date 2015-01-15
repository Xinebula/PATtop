import java.util.Scanner;



/**
 * Created by xiny on 2015/1/15.
 */
public class PAT1001 {
    static int[][] map;
    static int[] went;
    static int[] costIfLost;
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        int M,N;
        N = in.nextInt();
        M = in.nextInt();
        map = new int[N][N];//each x,y is the number of cities minus 1
        went = new int[N];//to use when checking
        costIfLost = new int[N];//the importance to protect certain city
        for(int i=0;i<M;i++){//import data
            int x,y;
            int k;
            x=in.nextInt()-1;
            y=in.nextInt()-1;
            k=in.nextInt();
            if(in.nextInt()==0)
                k*=-1;
            map[x][y] = k;
            map[y][x] = k;
        }
        for(int i=0;i<N;i++){//get the totalCost of each city if lost
            int[][] saveData = new int[N][N];
            for(int j=0;j<N;j++)
            saveData[j] = map[j].clone();//save the map
            for(int j=0;j<N;j++){//wipe the certain city
                map[i][j]=0;
                map[j][i]=0;
            }
            if(i==0)
                costIfLost[0] = check(N,1,0);
            else
                costIfLost[i] = check(N,0,0);
            map = saveData;//reload the map
        }
        int maxCostIfLost=-1;
        for(int i=0;i<N;i++)//find the biggest cost if one city is lost
            if(costIfLost[i]>maxCostIfLost)
                maxCostIfLost=costIfLost[i];
        if(maxCostIfLost==0)
            System.out.print(0);
        else{
            String output = "";
            for(int i=0;i<N;i++)
                if(costIfLost[i]==maxCostIfLost){
                    output+=(i+1);
                    output+=' ';
                }
            output=output.trim();
            System.out.print(output);
        }
    }

    static int check(int N,int x,int totalCost){
        if(goTo(N,x,0)==N-1){
            for(int i=0;i<N;i++)
                went[i]=0;//reset the went array
            return totalCost;
        }
        int[] cheapestRoute ={Integer.MAX_VALUE,0,0};//{minCost,x,y}
        cheapestRoute = findCheapestRoute(N,x,cheapestRoute);
        totalCost+=cheapestRoute[0];
        map[cheapestRoute[1]][cheapestRoute[2]]*=-1;
        map[cheapestRoute[2]][cheapestRoute[1]]*=-1;//repair the certain route
        for(int i=0;i<N;i++)
            went[i]=0;//reset the went array
        return check(N,x,totalCost);
    }

    static int goTo(int N,int x,int numWent){
        went[x]=1;
        numWent++;
        for(int i=0;i<N;i++){
            if(went[i]==0&&map[x][i]>0)
                numWent=goTo(N,i,numWent);
        }
        return numWent;
    }
    static int[] findCheapestRoute(int N,int x,int[] cheapestRoute){
        went[x]=2;
        for(int i=0;i<N;i++){
            if(went[i]==0&&map[x][i]<0&&map[x][i]*-1<cheapestRoute[0]){
                cheapestRoute[0]=map[x][i]*-1;
                cheapestRoute[1]=x;
                cheapestRoute[2]=i;
            }
        }
        for(int i=0;i<N;i++){
            if(went[i]==1&&map[x][i]>0)
                cheapestRoute= findCheapestRoute(N,i,cheapestRoute);
        }
        return cheapestRoute;

    }
}
