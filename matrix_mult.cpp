#include <iostream>
#include <pthread.h>

#include <cstdlib>
#include <ctime>

using namespace std;


constexpr int N = 1000;

enum return_codes
{
    SUCCESS,
    FAILURE,
    DIMENSIONS_ERROR,
    OUT_OF_BOUNDS
};

template <typename T>
struct ARGS
{

    T **m1, **m2, **m3, r;
};

#ifdef DO_PARALLEL
int step_i = 0;

void *
par_mult(void *arg)
{
    ARGS<int> *lol = (ARGS<int> *)arg;
    int core = step_i++;
    int MAX = lol->r;
    // Each thread computes 1/4th of matrix multiplication
    for (int i = core * MAX / 4; i < (core + 1) * MAX / 4; i++)
        for (int j = 0; j < MAX; j++)
            for (int k = 0; k < MAX; k++)
            {
                lol->m3[i][j] += lol->m1[i][k] * lol->m2[k][j];
                lol->m3[i][j] %= 100;
                lol->m3[i][j] += 1;
            }
}
#endif

template <typename T>
int multiply(T **matA, T **matB, T **matC, int r)
{
    if (r >= N)
        return OUT_OF_BOUNDS;

    int i, j, k;

    for (i = 0; i < r; i++)
    {
        for (j = 0; j < r; j++)
        {
            matC[i][j] = 0;
            for (k = 0; k < r; k++)
            {
                matC[i][j] += matA[i][k] * matB[k][j];
                matC[i][j] %= 100;
                matC[i][j] += 1;
            }
        }
    }

    return SUCCESS;
}

// template <typename T>
// ostream& operator << (ostream& o, const T& mat[][N]){

//     for(int i=0;i<)
// }

int main()
{
    srand((int)time(0));

    int **m1, **m2, **m3;
    int r;

    //cin >> r;
    r = 999;
    if (r > N)
    {
        return DIMENSIONS_ERROR;
    }

    m1 = new int *[r];
    m2 = new int *[r];
    m3 = new int *[r];

    for (int i = 0; i < r; i++)
    {
        m1[i] = new int[r];
        m2[i] = new int[r];
        m3[i] = new int[r];
    }

    for (int i = 0; i < r; i++)
    {
        for (int j = 0; j < r; j++)
        {
            m1[i][j] = (rand() % 100) + 1;
        }
    }
    for (int i = 0; i < r; i++)
    {
        for (int j = 0; j < r; j++)
        {
            m2[i][j] = (rand() % 100) + 1;
        }
    }

#ifdef DO_PARALLEL
    constexpr int MAX_THREAD = 4;
    pthread_t threads[MAX_THREAD];

    ARGS<int> *lol = new ARGS<int>;

    lol->m1 = m1;
    lol->m2 = m2;
    lol->m3 = m3;
    lol->r = r;

    for (int i = 0; i < MAX_THREAD; i++)
    {
        pthread_create(&threads[i], NULL, par_mult, (void *)lol);
    }

    for (int i = 0; i < MAX_THREAD; i++)
        pthread_join(threads[i], NULL);

    int rc = SUCCESS;
#else
    int rc = multiply(m1, m2, m3, r);
#endif

#ifdef CHECK
    int **m4 = new int *[r];
    for (int i = 0; i < r; i++)
    {
        m4[i] = new int[r];
    }

    multiply(m1, m2, m4, r);
    for (int i = 0; i < r; i++)
    {
        for (int j = 0; j < r; j++)
        {
            if (m4[i][j] != m3[i][j])
            {
                rc = FAILURE;
                break;
            }
            if (rc == FAILURE)
                break;
        }
    }

    for (int i = 0; i < r; i++)
        delete[] m4[i];
    delete[] m4;

#endif

    // for (int i = 0; i < r; i++)
    // {
    //     for (int j = 0; j < r; j++)
    //     {
    //         cout << m3[i][j] << " ";
    //     }
    //     cout << "\n";
    // }
    if (rc == SUCCESS)
    {

        cout << "SUCCESS";
    }
    else
    {
        cout << "Multiply failed ";
    }
    for (int i = 0; i < r; i++)
    {
        delete[] m1[i];
        delete[] m2[i];
        delete[] m3[i];
    }
    delete m1;
    delete m2;
    delete m3;

    return SUCCESS;
}