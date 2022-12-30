package xyz.hiziki.gun.team;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import xyz.hiziki.gun.team.TeamEnum;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardSetter
{
    public List<Team> teams;

    public List<Score> scoreList;

    public Scoreboard ScoreBord;

    private List<String> tmpPlayerNameList;

    public ScoreboardSetter(Server server)
    {
        ScoreboardManager manager = server.getScoreboardManager();

        ScoreBord = manager.getMainScoreboard();
        ScoreBord.clearSlot(DisplaySlot.SIDEBAR);

        teams = new ArrayList<>();

        for (Team team : ScoreBord.getTeams())
        {
            team.unregister();
        }

        // チームが既に登録されているかどうか確認し、
        // 登録されていないなら新規作成します。

        for (TeamEnum team : TeamEnum.values())
        {
            if (ScoreBord.getTeam(team.getTeamName()) == null)
            {
                Team tmpTeam = ScoreBord.registerNewTeam(team.getTeamName());
                tmpTeam.setColor(team.getTeamColor());
                tmpTeam.setPrefix(team.getViewTeamName());
                tmpTeam.setDisplayName(team.getPrefix());
                tmpTeam.setAllowFriendlyFire(false);
                teams.add(tmpTeam);
            }
        }

        tmpPlayerNameList = new ArrayList<>();

        for (Player player : server.getOnlinePlayers())
        {
            player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
            tmpPlayerNameList.add(player.getName());
        }

        firstSetScoreView();
        setTeamMember();
    }

    public void setTeamMember()
    {
        for (Team team : teams)
        {
            for (String tmp : team.getEntries())
            {
                team.removeEntry(tmp);
            }
        }
        while (!tmpPlayerNameList.isEmpty())
        {
            for (Team team: teams)
            {
                int No = (int) (Math.random() * ((tmpPlayerNameList.size() - 1)));
                team.addEntry(tmpPlayerNameList.get(No));
                tmpPlayerNameList.remove(No);
                if (tmpPlayerNameList.isEmpty()) break;
            }
        }
    }

    public void firstSetScoreView()
    {
        Objective objective = ScoreBord.getObjective("GunScore");

        if (objective == null)
        {
            objective = ScoreBord.registerNewObjective("GunScore", "Kill", "スコア状況");
        }

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("キル状況");

        scoreList = new ArrayList<>();

        for (TeamEnum team : TeamEnum.values())
        {
            Score tmpScore = objective.getScore(team.getTeamColor() + team.getViewTeamName()+"：");
            tmpScore.setScore(0);
            scoreList.add(tmpScore);
        }
    }

    public void pointCheck(Player Killer, Player target)
    {
        for (Team team : teams)
        {
            if (team.hasEntry(Killer.getName()))
            {
                if (team.hasEntry(target.getName()))
                {
                    teamPoint(TeamEnum.getEnum(team.getName()), -1);
                }
                else
                {
                    teamPoint(TeamEnum.getEnum(team.getName()), 1);
                }
            }
        }
    }

    public void teamPoint(TeamEnum TeamKind, int count)
    {
        int tmp = scoreList.get(TeamKind.ordinal()).getScore();
        tmp = tmp + count;
        scoreList.get(TeamKind.ordinal()).setScore(tmp);
    }
}
